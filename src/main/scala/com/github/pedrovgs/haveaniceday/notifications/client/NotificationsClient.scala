package com.github.pedrovgs.haveaniceday.notifications.client

import com.github.pedrovgs.haveaniceday.notifications.client.model._
import com.github.pedrovgs.haveaniceday.notifications.model.{FirebaseConfig, Notification, SendNotificationError}
import com.github.pedrovgs.haveaniceday.smiles.model.{ErrorSendingNotification, Smile}
import com.google.inject.Inject
import com.twitter.inject.Logging
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import scala.concurrent.Future
import scalaj.http.{Http, HttpRequest, HttpResponse}
import scala.concurrent.ExecutionContext.Implicits.global

class NotificationsClient @Inject()(config: FirebaseConfig) extends Logging {

  def sendSmileToEveryUser(smile: Smile, smileNumber: Int): Future[Either[ErrorSendingNotification, Smile]] = {
    val notification = generateNotificationFromSmile(smile, smileNumber)
    sendNotification(notification).map {
      case Right(_)                => Right(smile)
      case Left(notificationError) => Left(ErrorSendingNotification(smile, notificationError.message))
    }
  }

  private def sendNotification(notification: Notification): Future[Either[SendNotificationError, Notification]] = {
    Future {
      val response = sendPostRequestToFirebase(notification)
      info(s"Push notification sent with response: $response")
      val responseBody = response.body
      if (response.isSuccess) {
        val firebaseResponse = decode[FirebaseResponse](responseBody)
        firebaseResponse match {
          case Right(FirebaseResponse(Some(_), None)) => Right(notification)
          case Right(FirebaseResponse(None, Some(_))) =>
            Left(SendNotificationError(response.code, responseBody))
          case _ =>
            Left(SendNotificationError(response.code, responseBody))
        }
      } else {
        Left(SendNotificationError(response.code, responseBody))
      }
    }
  }

  private def sendPostRequestToFirebase(notification: Notification): HttpResponse[String] = {
    val body = generateRequestBody(config.firebaseDefaultTopic, notification)
    val request: HttpRequest = Http(config.firebaseUrl)
      .header("Authorization", "key=" + config.firebaseApiKey)
      .header("Content-Type", "application/json")
    info(s"Generating push notification data with content: $body")
    request.postData(body).asString
  }

  private def generateRequestBody(to: String, notification: Notification): String = {
    FirebaseNotification.fromNotification(to, notification).asJson.toString
  }

  private def generateNotificationFromSmile(smile: Smile, smileNumber: Int): Notification = {
    val title    = s"Have a nice day #$smileNumber 😃"
    val message  = smile.description.getOrElse(title)
    val photoUrl = smile.photo
    Notification(smile.id, title, message, photoUrl)
  }

}
