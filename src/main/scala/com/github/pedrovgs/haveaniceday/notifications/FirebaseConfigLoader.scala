package com.github.pedrovgs.haveaniceday.notifications

import classy.generic._
import com.github.pedrovgs.haveaniceday.notifications.model.FirebaseConfig
import com.typesafe.config.Config
import classy.config._

object FirebaseConfigLoader {

  def loadFirebaseConfig(config: Config): Option[FirebaseConfig] = {
    val decoder = deriveDecoder[Config, FirebaseConfig]
    decoder(config).toOption
  }

}
