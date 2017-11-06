package com.github.pedrovgs.haveaniceday.notifications

object model {

  case class FirebaseConfig(firebaseUrl: String, firebaseApiKey: String, firebaseDefaultTopic: String)

  case class Notification(id: Long, title: String, messgae: String, photoUrl: Option[String])

  case class SendNotificationError(code: Int, message: String)

}
