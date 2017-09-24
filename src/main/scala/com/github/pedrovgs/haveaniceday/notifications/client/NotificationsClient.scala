package com.github.pedrovgs.haveaniceday.notifications.client

import com.github.pedrovgs.haveaniceday.notifications.client.model._
import com.github.pedrovgs.haveaniceday.notifications.model.{FirebaseConfig, Notification}
import com.google.inject.Inject
import io.circe.syntax._

import scalaj.http.{Http, HttpRequest}

class NotificationsClient @Inject()(config: FirebaseConfig) {

  def sendNotification(notification: Notification) = {
    val body = generateRequestBody(notification)
    val request: HttpRequest = Http("https://fcm.googleapis.com/fcm/send")
      .header("Authorization", "key=" + config.firebaseApiKey)
      .header("Content-Type", "application/json")
    request.postData(body)
  }

  private def generateRequestBody(notification: Notification): String = {
    val firebaseNotificationData =
      FirebaseNotificationData(notification.title, notification.messgae, notification.photoUrl)
    val firebaseNotification = FirebaseNotification(null, firebaseNotificationData)
    firebaseNotification.asJson.toString()
  }
}
