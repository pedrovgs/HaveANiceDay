package com.github.pedrovgs.haveaniceday.notifications.client

import com.github.pedrovgs.haveaniceday.notifications.client.model.FirebaseNotification
import com.github.pedrovgs.haveaniceday.notifications.model.{FirebaseConfig, Notification, SendNotificationError}
import com.github.tomakehurst.wiremock.client.WireMock._
import extensions.futures._
import generators.notifications._
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks
import specs.StubbingHttpSpec
import specs.StubbingHttpSpec._

object NotificationsClientSpec {
  private val pushNotificationsPath = "/send"
  private val firebaseUrl           = baseUrl + pushNotificationsPath
  private val apiKey                = "any_firebase_api_key"
  private val topic                 = "/topics/haveANiceDay"
  private val errorBody             = "Something went wrong in your config"
  private val errorStatusCode       = 400
}

class NotificationsClientSpec extends StubbingHttpSpec with Matchers with PropertyChecks {

  import NotificationsClientSpec._

  private val firebaseConfig = FirebaseConfig(firebaseUrl, apiKey, topic)
  private val client         = new NotificationsClient(firebaseConfig)

  "NotificationsClient" should "return the notification sent if the push notification was sent properly" in {
    stubFor(
      post(urlEqualTo(pushNotificationsPath))
        .willReturn(aResponse().withStatus(200)))
    forAll(arbitraryNotification) { notification: Notification =>
      val result = client.sendNotificationToEveryUser(notification).awaitForResult.toOption.get

      result shouldBe notification
    }
  }

  it should "send the content-type header expected by firebase servers" in {
    stubFor(
      post(urlEqualTo(pushNotificationsPath))
        .withHeader("Content-type", equalTo("application/json"))
        .willReturn(aResponse().withStatus(200)))
    forAll(arbitraryNotification) { notification: Notification =>
      val result = client.sendNotificationToEveryUser(notification).awaitForResult.toOption.get

      result shouldBe notification
    }
  }

  it should "send the authorization header expected by firebase servers conatining the api key" in {
    stubFor(
      post(urlEqualTo(pushNotificationsPath))
        .withHeader("Authorization", equalTo("key=" + apiKey))
        .willReturn(aResponse().withStatus(200)))
    forAll(arbitraryNotification) { notification: Notification =>
      val result = client.sendNotificationToEveryUser(notification).awaitForResult.toOption.get

      result shouldBe notification
    }
  }

  it should "send the notification using a body composed with the notification info" in {
    forAll(arbitraryNotification) { notification: Notification =>
      val body = FirebaseNotification.fromNotification(topic, notification).asJson.toString
      stubFor(
        post(urlEqualTo(pushNotificationsPath))
          .withRequestBody(equalToJson(body))
          .willReturn(aResponse().withStatus(200)))

      val result = client.sendNotificationToEveryUser(notification).awaitForResult.toOption.get

      result shouldBe notification
    }
  }

  it should "return the status code error and the body if something goes wrong" in {
    forAll(arbitraryNotification) { notification: Notification =>
      stubFor(
        post(urlEqualTo(pushNotificationsPath))
          .willReturn(aResponse().withStatus(errorStatusCode).withBody(errorBody)))

      val result = client.sendNotificationToEveryUser(notification).awaitForResult

      result shouldBe Left(SendNotificationError(errorStatusCode, errorBody))
    }
  }

}
