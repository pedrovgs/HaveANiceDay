package com.github.pedrovgs.haveaniceday.notifications.config

import com.github.pedrovgs.haveaniceday.notifications.model.FirebaseConfig
import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

class FirebaseConfigLoaderSpec extends FlatSpec with Matchers {

  "FirebaseConfigLoader" should "read the values associated with the firebase configuration" in {
    val config = ConfigFactory.load("firebase/validFirebaseConfig.conf")

    val firebaseConfig = FirebaseConfigLoader.loadFirebaseConfig(config).get

    firebaseConfig shouldBe FirebaseConfig("https://fcm.googleapis.com/fcm/send",
                                           "firebase_api_key",
                                           "/topics/haveANiceDay")
  }

  it should "return none if the configuration file is empty" in {
    val config = ConfigFactory.load("firebase/emptyFirebaseConfig.conf")

    val firebaseConfig = FirebaseConfigLoader.loadFirebaseConfig(config)

    firebaseConfig shouldBe None
  }

  it should "return none if the configuration file does not contain the expected keys" in {
    val config = ConfigFactory.load("firebase/invalidFirebaseConfig.conf")

    val firebaseConfig = FirebaseConfigLoader.loadFirebaseConfig(config)

    firebaseConfig shouldBe None
  }

}
