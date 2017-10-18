package com.github.pedrovgs.haveaniceday.notifications.client

import com.github.pedrovgs.haveaniceday.notifications.model.Notification

object model {

  case class FirebaseNotificationData(title: String, message: String, photoUrl: Option[String])

  object FirebaseNotification {
    def fromNotification(to: String, notification: Notification): FirebaseNotification = {
      val data = FirebaseNotificationData(notification.title, notification.messgae, notification.photoUrl)
      FirebaseNotification(to, data)
    }
  }
  case class FirebaseNotification(to: String, data: FirebaseNotificationData)

  case class FirebaseResponse(message_id: Option[Long], failure: Option[Long])

}
