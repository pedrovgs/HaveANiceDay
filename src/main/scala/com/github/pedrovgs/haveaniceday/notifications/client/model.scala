package com.github.pedrovgs.haveaniceday.notifications.client

import io.circe._
import io.circe.generic.semiauto._

object model {

  case class FirebaseNotificationData(title: String, message: String, photoUrl: Option[String])

  case class FirebaseNotification(to: String, data: FirebaseNotificationData)

  implicit val firebaseNotificationDataDecoder: Decoder[FirebaseNotificationData] =
    deriveDecoder[FirebaseNotificationData]
  implicit val firebaseNotificationDataEncoder: Encoder[FirebaseNotificationData] =
    deriveEncoder[FirebaseNotificationData]
  implicit val firebaseNotificationDecoder: Decoder[FirebaseNotification] = deriveDecoder[FirebaseNotification]
  implicit val firebaseNotificationEncoder: Encoder[FirebaseNotification] = deriveEncoder[FirebaseNotification]

}
