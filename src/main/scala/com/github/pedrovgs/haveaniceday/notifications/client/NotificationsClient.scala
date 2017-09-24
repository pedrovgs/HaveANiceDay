package com.github.pedrovgs.haveaniceday.notifications.client

import com.github.pedrovgs.haveaniceday.notifications.client.model._
import com.github.pedrovgs.haveaniceday.notifications.model.{FirebaseConfig, Notification, SendNotificationError}
import com.google.inject.Inject
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.Future
import scalaj.http.{Http, HttpRequest, HttpResponse}
import scala.concurrent.ExecutionContext.Implicits.global

class NotificationsClient @Inject()(config: FirebaseConfig) {

  def sendNotificationToEveryUser(notification: Notification): Future[Either[SendNotificationError, Notification]] = {
    Future {
      val response = sendPostRequestToFirebase(notification)
      if (response.isSuccess) {
        Right(notification)
      } else {
        Left(SendNotificationError(response.code, response.body))
      }
    }
  }

  private def sendPostRequestToFirebase(notification: Notification): HttpResponse[String] = {
    val body = generateRequestBody(config.firebaseDefaultTopic, notification)
    val request: HttpRequest = Http(config.firebaseUrl)
      .header("Authorization", "key=" + config.firebaseApiKey)
      .header("Content-Type", "application/json")
    request.postData(body).asString
  }

  private def generateRequestBody(to: String, notification: Notification): String = {
    FirebaseNotification.fromNotification(to, notification).asJson.toString
  }
}
