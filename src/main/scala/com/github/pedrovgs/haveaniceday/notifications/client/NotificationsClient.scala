package com.github.pedrovgs.haveaniceday.notifications.client

import com.github.pedrovgs.haveaniceday.notifications.client.model._
import com.github.pedrovgs.haveaniceday.notifications.model.{FirebaseConfig, Notification}
import com.google.inject.Inject
import io.circe.generic.auto._
import io.circe.syntax._

import scalaj.http.{Http, HttpRequest, HttpResponse}

class NotificationsClient @Inject()(config: FirebaseConfig) {

  def sendNotification(notification: Notification) = {
    val response = sendPostRequestToFirebase(notification)
    if (response.isSuccess) {
      println("---------------")
      println("SUCCESS " + response.body)
      println("---------------")
    } else {
      println("---------------")
      println("ERROR" + response.code)
      println("ERROR" + response.body)
      println("---------------")
    }
  }

  private def sendPostRequestToFirebase(notification: Notification): HttpResponse[String] = {
    val body = generateRequestBody(notification)
    val request: HttpRequest = Http("https://fcm.googleapis.com/fcm/send")
      .header("Authorization", "key=" + config.firebaseApiKey)
      .header("Content-Type", "application/json")
    request.postData(body).asString
  }

  private def generateRequestBody(notification: Notification): String = {
    println("---------------")
    println("Notification " + notification)
    println("---------------")
    val firebaseNotificationData =
      FirebaseNotificationData(notification.title, notification.messgae, notification.photoUrl)
    val firebaseNotification = FirebaseNotification("", firebaseNotificationData)
    firebaseNotification.asJson.toString
  }
}
