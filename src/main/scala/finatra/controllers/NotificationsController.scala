package finatra.controllers

import com.github.pedrovgs.haveaniceday.notifications.client.NotificationsClient
import com.github.pedrovgs.haveaniceday.notifications.model.Notification
import com.google.inject.Inject
import com.jakehschwartz.finatra.swagger.SwaggerController
import com.twitter.finagle.http.Request
import io.swagger.models.Swagger

class NotificationsController @Inject()(s: Swagger, notificationsClient: NotificationsClient) extends SwaggerController {
  implicit protected val swagger: Swagger = s

  postWithDoc("/dailyNotification") { o =>
    o.tag("Notifications")
      .summary("Sends notifications to all the registered devices once a day")
      .responseWith(201)
  } { _: Request =>
    notificationsClient.sendNotification(Notification("Title", "Message", Some("Photo URL")))
    response.ok
  }
}
