package com.github.pedrovgs.haveaniceday.smiles.config

import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGeneratorConfig
import com.typesafe.config.Config
import scala.collection.JavaConverters._

object SmilesGeneratorConfigLoader {

  def loadSmilesGeneratorConfig(config: Config): Option[SmilesGeneratorConfig] = {
    val twitterAccounts    = config.getStringList("twitterAccounts").asScala.toList
    val scheduleTasks      = config.getBoolean("scheduleTasks")
    val extractionSchedule = config.getString("extractionSchedule")
    val generationSchedule = config.getString("generationSchedule")
    Some(SmilesGeneratorConfig(twitterAccounts, scheduleTasks, extractionSchedule, generationSchedule))
  }

}
