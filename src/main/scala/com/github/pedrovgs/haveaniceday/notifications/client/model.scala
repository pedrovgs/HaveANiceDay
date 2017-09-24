package com.github.pedrovgs.haveaniceday.notifications.client

object model {

  case class FirebaseNotificationData(title: String, message: String, photoUrl: Option[String])

  case class FirebaseNotification(to: String, data: FirebaseNotificationData)

}
