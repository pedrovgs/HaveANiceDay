package com.github.pedrovgs.haveaniceday.smiles.config

import classy.config._
import classy.generic._
import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGeneratorConfig
import com.typesafe.config.Config

object SmilesGeneratorConfigLoader {

  def loadSmilesGeneratorConfig(config: Config): Option[SmilesGeneratorConfig] = {
    val decoder = deriveDecoder[Config, SmilesGeneratorConfig]
    decoder(config).toOption
  }

}
