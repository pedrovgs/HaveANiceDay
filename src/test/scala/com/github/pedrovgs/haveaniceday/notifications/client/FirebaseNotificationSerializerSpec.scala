package com.github.pedrovgs.haveaniceday.notifications.client

import com.github.pedrovgs.haveaniceday.notifications.client.model.{FirebaseNotification, FirebaseNotificationData}
import org.scalatest.{FlatSpec, Matchers}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe._
import io.circe.parser._
import org.scalatest.prop.PropertyChecks
import specs.TestResources
import generators.notifications._

object FirebaseNotificationSerializerSpec {
  private val anyId         = 1
  private val anyToResource = "anyResource"
  private val anyTitle      = "title"
  private val anyMessage    = "message"
  val completeNotification =
    FirebaseNotification(anyToResource, FirebaseNotificationData(anyId, anyTitle, anyMessage, Some("photo url")))
  val incompleteNotification =
    FirebaseNotification(anyToResource, FirebaseNotificationData(anyId, anyTitle, anyMessage, None))
}
class FirebaseNotificationSerializerSpec extends FlatSpec with Matchers with TestResources with PropertyChecks {
  import FirebaseNotificationSerializerSpec._

  "FirebaseNotification serializer" should "serialize a complete notification" in {
    val serializedNotification = completeNotification.asJson

    serializedNotification shouldBe parse(contentFromResource("/notifications/completeNotification.json")).toOption.get
  }

  it should "serialize an incomplete notification" in {
    val serializedNotification = incompleteNotification.asJson

    serializedNotification shouldBe parse(contentFromResource("/notifications/incompleteNotification.json")).toOption.get
  }

  it should "pass the loopback property" in {
    forAll(arbitraryFirebaseNotification) { notification: FirebaseNotification =>
      val serializedNotification = notification.asJson
      val deserializedNotification =
        parse(serializedNotification.toString).toOption.get.as[FirebaseNotification].toOption.get

      notification shouldBe deserializedNotification
    }
  }
}
