package com.github.pedrovgs.haveaniceday.notifications

import com.github.pedrovgs.haveaniceday.notifications.model.FirebaseConfig
import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

class FirebaseConfigLoaderSpec extends FlatSpec with Matchers {

  "FirebaseConfigLoader" should "read the config value associated to the api key" in {
    val config = ConfigFactory.load("firebase/validFirebaseConfig.conf")

    val firebaseConfig = FirebaseConfigLoader.loadFirebaseConfig(config).get

    firebaseConfig shouldBe FirebaseConfig("firebase_api_key")
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
