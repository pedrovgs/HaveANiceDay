package com.github.pedrovgs.haveaniceday.smiles

import com.github.pedrovgs.haveaniceday.notifications.client.NotificationsClient
import com.github.pedrovgs.haveaniceday.notifications.model.{Notification, SendNotificationError}
import com.github.pedrovgs.haveaniceday.smiles.apiclient.TwitterClient
import com.github.pedrovgs.haveaniceday.smiles.config.SmilesGeneratorConfigLoader
import com.github.pedrovgs.haveaniceday.smiles.model._
import com.github.pedrovgs.haveaniceday.smiles.storage.{
  SmilesExtractionsRepository,
  SmilesGenerationsRepository,
  SmilesRepository
}
import com.github.pedrovgs.haveaniceday.utils.Clock
import com.typesafe.config.ConfigFactory
import extensions.futures._
import extensions.scalacheck
import extensions.scalacheck.RichGen
import generators.smiles._
import org.joda.time.DateTime
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.verification.VerificationMode
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.PropertyChecks
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import specs.InMemoryDatabase

import scala.concurrent.Future

class SmilesGeneratorSpec
    extends FlatSpec
    with Matchers
    with InMemoryDatabase
    with BeforeAndAfter
    with PropertyChecks
    with MockitoSugar {

  private val config = SmilesGeneratorConfigLoader
    .loadSmilesGeneratorConfig(ConfigFactory.load("smilesGenerator/validSmilesGenerator.conf"))
    .get
  private val clock                 = mock[Clock]
  private val twitterClient         = mock[TwitterClient]
  private val notificationsClient   = mock[NotificationsClient]
  private val extractionsRepository = new SmilesExtractionsRepository(database)
  private val generationsRepository = new SmilesGenerationsRepository(database, clock)
  private val smilesRepository      = new SmilesRepository(database)

  private val smilesGenerator = new SmilesGenerator(config,
                                                    twitterClient,
                                                    extractionsRepository,
                                                    generationsRepository,
                                                    smilesRepository,
                                                    notificationsClient,
                                                    clock)

  before {
    when(clock.now).thenReturn(new DateTime())
    reset(notificationsClient)
    resetDatabase()
  }

  after {
    resetDatabase()
  }

  "SmilesGenerator smiles extraction process" should "extract smiles from twitter even if it's the first time" in {
    forAll(RichGen.listOfMaxN(10, arbitrarySmile)) { smiles =>
      givenTwitterClientReturns(smiles)

      val result = smilesGenerator.extractSmiles().awaitForResult

      assertSmilesAreExtractedProperly(smiles, result.right.get)
      resetDatabase()
    }
  }

  it should "extract smiles from twitter using the last extraction date" in {
    forAll(RichGen.listOfMaxN(1, arbitrarySmile), RichGen.listOfMaxN(10, arbitrarySmile)) {
      (firstSmilesExtracted, secondSmilesExtracted) =>
        givenTwitterClientReturns(firstSmilesExtracted, firstExtraction = true)
        smilesGenerator.extractSmiles().awaitForResult

        givenTwitterClientReturns(secondSmilesExtracted, firstExtraction = false)
        val result = smilesGenerator.extractSmiles().awaitForResult.right.get

        assertSmilesAreExtractedProperly(secondSmilesExtracted, result)
        resetDatabase()
    }
  }

  it should "return an error if there is something wrong extracting smiles" in {
    forAll(arbitrarySmilesExtractionError) { error =>
      givenTwitterReturnsAnErrorExtractingSmiles(error)

      val result = smilesGenerator.extractSmiles().awaitForResult

      result shouldBe Left(error)
    }
  }

  it should "return an error if there is something wrong extracting smiles even if previous extractions went well" in {
    forAll(RichGen.listOfMaxN(10, arbitrarySmile), arbitrarySmilesExtractionError) { (firstSmilesExtracted, error) =>
      givenTwitterClientReturns(firstSmilesExtracted, firstExtraction = true)
      smilesGenerator.extractSmiles().awaitForResult
      givenTwitterReturnsAnErrorExtractingSmiles(error)

      val result = smilesGenerator.extractSmiles().awaitForResult

      result shouldBe Left(error)
    }
  }

  "SmilesGenerator generation process" should "return an error if there are no smiles to generate because there was no any previous extracion" in {
    val result = smilesGenerator.generateSmiles().awaitForResult

    result shouldBe Left(NoExtractedSmilesFound)
    resetDatabase()
  }

  it should "does not send any notification if there are no smiles to generate" in {
    smilesGenerator.generateSmiles().awaitForResult

    verify(notificationsClient, never()).sendSmileToEveryUser(any[Smile], any[Int])
    resetDatabase()
  }

  it should "generate one random not sent smile as a notification sending it to our users" in {
    forAll(RichGen.nonEmptyListOfMaxN(10, arbitraryNotSentSmile)) { smiles =>
      givenTwitterClientReturns(smiles)
      givenTheNotificationsClientSendsTheNotification()

      val extractedSmiles = smilesGenerator.extractSmiles().awaitForResult.right.get
      val result          = smilesGenerator.generateSmiles().awaitForResult.right.get

      assertSmileWasSent(extractedSmiles, result, 1, times(1))
      reset(notificationsClient)
      resetDatabase()
    }
  }

  it should "generate smiles until there is no more content to send" in {
    forAll(RichGen.nonEmptyListOfMaxN(10, arbitraryNotSentSmile)) { smiles =>
      givenTwitterClientReturns(smiles)
      givenTheNotificationsClientSendsTheNotification()
      var extractedSmiles = smilesGenerator.extractSmiles().awaitForResult.right.get

      (1 to extractedSmiles.length).foreach { smileNumber =>
        val result = smilesGenerator.generateSmiles().awaitForResult.right.get

        extractedSmiles = assertSmileWasSent(extractedSmiles, result, smileNumber, atLeastOnce())
      }
      val noMoreSmilesResult = smilesGenerator.generateSmiles().awaitForResult
      noMoreSmilesResult shouldBe Left(NoExtractedSmilesFound)
      resetDatabase()
    }
  }

  it should "indicate return an error if there was something wrong while sending the notification" in {
    forAll(RichGen.nonEmptyListOfMaxN(10, arbitraryNotSentSmile)) { smiles =>
      givenThereIsAnErrorWhileGeneratingTheNotification()
      givenTwitterClientReturns(smiles)

      smilesGenerator.extractSmiles().awaitForResult
      val result = smilesGenerator.generateSmiles().awaitForResult

      result.isLeft shouldBe true
      resetDatabase()
    }
  }

  private def assertSmileWasSent(extractedSmiles: Seq[Smile],
                                 result: Smile,
                                 smileNumber: Int,
                                 numberOfNotificationsSent: VerificationMode): Seq[Smile] = {
    val smilesSortedByLikes = extractedSmiles
      .filterNot(_.sent)
      .sortBy(smile => (smile.numberOfLikes, smile.id))
      .reverse
    smilesSortedByLikes.map(_.id).contains(result.id) shouldBe true
    result.sent shouldBe true
    result.sentDate shouldBe Some(clock.now)
    result.number shouldBe Some(smileNumber)
    val expectedNotificationSent = smilesSortedByLikes.find(_.id == result.id).get
    verifySmileSent(expectedNotificationSent, smileNumber, numberOfNotificationsSent)
    extractedSmiles.filterNot(_ == result)
  }

  private def verifySmileSent(expectedSmile: Smile, smileNumber: Int, numberOfNotificationsSent: VerificationMode) = {
    val id               = expectedSmile.id
    val title            = s"Have a nice day #$smileNumber 😃"
    val message          = expectedSmile.description.getOrElse(title)
    val photoUrl         = expectedSmile.photo
    val notificationSent = Notification(id, title, message, photoUrl)
    verify(notificationsClient, numberOfNotificationsSent).sendSmileToEveryUser(expectedSmile, smileNumber)
  }

  private def givenTheNotificationsClientSendsTheNotification() = {
    val result = Future.successful(Right(mock[Smile]))
    when(notificationsClient.sendSmileToEveryUser(any[Smile], any[Int])).thenReturn(result)
  }

  private def givenThereIsAnErrorWhileGeneratingTheNotification(): ErrorSendingNotification = {
    val error = ErrorSendingNotification(mock[Smile], SendNotificationError(400, "Invalid topic").message)
    when(notificationsClient.sendSmileToEveryUser(any[Smile], any[Int])).thenReturn(Future.successful(Left(error)))
    error
  }

  private def givenTwitterReturnsAnErrorExtractingSmiles(error: SmilesExtractionError): Unit = {
    val result = Future.successful(Left(error))
    when(twitterClient.smilesFrom(any[Seq[String]], any[DateTime])).thenReturn(result)
  }

  private def assertSmilesAreExtractedProperly(expectedSmiles: Seq[Smile], result: Seq[Smile]): Unit = {
    val resultSize   = result.length
    val expectedSize = expectedSmiles.length
    resultSize shouldBe expectedSize
    result.foreach { smile =>
      val smileId = smile.id
      expectedSmiles.map(_.copy(id = smileId)).contains(smile) shouldBe true
    }
  }

  private def givenTwitterClientReturns(smiles: Seq[Smile], firstExtraction: Boolean = true) = {
    val accounts  = config.twitterAccounts
    val result    = Future.successful(Right(smiles))
    val sinceDate = if (firstExtraction) clock.now.minusMonths(1) else clock.now
    when(twitterClient.smilesFrom(accounts, sinceDate)).thenReturn(result)
  }

}
