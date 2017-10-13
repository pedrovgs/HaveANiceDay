package com.github.pedrovgs.haveaniceday.smiles.apiclient

import com.github.pedrovgs.haveaniceday.extensions.date._
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{RatedData, Tweet}
import com.github.pedrovgs.haveaniceday.smiles.model.{Smile, SmilesExtractionResult, Source, UnknownError}
import org.joda.time.DateTime

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TwitterClient {

  private val restClient = TwitterRestClient()

  def smilesFrom(accounts: Seq[String], extractionDate: DateTime): Future[SmilesExtractionResult] = {
    val eventualSmiles = accounts.map { account =>
      restClient
        .userTimelineForUserId(account.toLong, exclude_replies = true, include_rts = false, count = Int.MaxValue)
        .flatMap { timeline =>
          Future.successful(toSmiles(extractionDate, timeline))
        }
    }
    Future.sequence(eventualSmiles).map(_.flatten).map(Right(_)).recover {
      case e => Left(UnknownError(e.getMessage))
    }
  }

  private def toSmiles(extractionDate: DateTime, timeline: RatedData[Seq[Tweet]]): Seq[Smile] = {
    val validTweets = timeline.data.filter(_.created_at.compareTo(extractionDate.toDate) < 0)
    validTweets.map { tweet =>
      val url   = "TODO" //TODO: Extract URL here for tweets using the user id and the id. Debug and review the values :)
      val photo = tweet.extended_entities.flatMap(_.media.headOption.map(_.display_url))
      Smile(tweet.id, tweet.created_at, photo, Some(tweet.text), Source.Twitter, url, sent = false, None, None)
    }
  }
}
