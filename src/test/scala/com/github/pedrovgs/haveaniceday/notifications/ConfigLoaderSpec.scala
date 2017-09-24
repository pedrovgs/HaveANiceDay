package com.github.pedrovgs.haveaniceday.notifications

import com.github.pedrovgs.haveaniceday.notifications.model.FirebaseConfig
import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

class ConfigLoaderSpec extends FlatSpec with Matchers {

  "ConfigLoader" should "read the config value associated to the api key" in {
    val loader = new ConfigLoader(ConfigFactory.load("firebase/validFirebaseConfig.conf"))

    val firebaseConfig = loader.loadFirebaseConfig().get

    firebaseConfig shouldBe FirebaseConfig("firebase_api_key")
  }

  it should "return none if the configuration file is empty" in {
    val loader = new ConfigLoader(ConfigFactory.load("firebase/emptyFirebaseConfig.conf"))

    val firebaseConfig = loader.loadFirebaseConfig()

    firebaseConfig shouldBe None
  }

  it should "return none if the configuration file does not contain the expected keys" in {
    val loader = new ConfigLoader(ConfigFactory.load("firebase/invalidFirebaseConfig.conf"))

    val firebaseConfig = loader.loadFirebaseConfig()

    firebaseConfig shouldBe None
  }

}
