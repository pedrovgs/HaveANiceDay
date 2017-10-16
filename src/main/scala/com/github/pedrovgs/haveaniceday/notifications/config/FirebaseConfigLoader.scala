package com.github.pedrovgs.haveaniceday.notifications.config

import com.github.pedrovgs.haveaniceday.notifications.model.FirebaseConfig
import com.typesafe.config.Config

object FirebaseConfigLoader {

  def loadFirebaseConfig(config: Config): Option[FirebaseConfig] = {
    try {
      val firebaseUrl          = config.getString("firebaseUrl")
      val firebaseApiKey       = config.getString("firebaseApiKey")
      val firebaseDefaultTopic = config.getString("firebaseDefaultTopic")
      Some(FirebaseConfig(firebaseUrl, firebaseApiKey, firebaseDefaultTopic))
    } catch {
      case _: Throwable => None
    }
  }

}
