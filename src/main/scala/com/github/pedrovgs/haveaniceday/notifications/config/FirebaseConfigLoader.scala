package com.github.pedrovgs.haveaniceday.notifications.config

import classy.config._
import classy.generic._
import com.github.pedrovgs.haveaniceday.notifications.model.FirebaseConfig
import com.typesafe.config.Config

object FirebaseConfigLoader {

  def loadFirebaseConfig(config: Config): Option[FirebaseConfig] = {
    val decoder = deriveDecoder[Config, FirebaseConfig]
    decoder(config).toOption
  }

}
