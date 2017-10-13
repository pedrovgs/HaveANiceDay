package com.github.pedrovgs.haveaniceday.smiles.apiclient

import javax.inject.Inject

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{RatedData, Tweet}
import com.github.pedrovgs.haveaniceday.extensions.date._
import com.github.pedrovgs.haveaniceday.smiles.model.{Smile, SmilesExtractionResult, Source, UnknownError}
import com.twitter.inject.Logging
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TwitterClient @Inject()() extends Logging {

  private val restClient = TwitterRestClient()

  def smilesFrom(accounts: Seq[String], extractionDate: DateTime): Future[SmilesExtractionResult] = {
    val eventualSmiles = accounts.map { account =>
      info(s"Extracting smiles for Twitter account with id: $account")
      restClient
        .userTimelineForUserId(account.toLong, exclude_replies = true, include_rts = false)
        .flatMap { timeline =>
          info(s"The timeline obtained for $account contains ${timeline.data.size} accounts")
          info(s"Rate limit account remaining value = ${timeline.rate_limit.remaining}")
          Future.successful(toSmiles(extractionDate, timeline))
        }
    }
    Future.sequence(eventualSmiles).map(_.flatten).map(Right(_)).recover {
      case e => Left(UnknownError(e.getMessage))
    }
  }

  private def toSmiles(extractionDate: DateTime, timeline: RatedData[Seq[Tweet]]): Seq[Smile] = {
    val validTweets = timeline.data
      .filter(_.created_at.compareTo(extractionDate.toDate) < 0)
      .filter(_.extended_entities.map(_.media).nonEmpty)
    validTweets.map { tweet =>
      val url           = s"https://twitter.com/${tweet.user.get.screen_name}/status/${tweet.id}"
      val photo         = tweet.extended_entities.flatMap(_.media.headOption.map(_.media_url_https))
      val numberOfLikes = tweet.favorite_count + tweet.retweet_count
      Smile(tweet.id,
            tweet.created_at,
            photo,
            Some(tweet.text),
            Source.Twitter,
            url,
            numberOfLikes,
            sent = false,
            None,
            None)
    }
  }
}
