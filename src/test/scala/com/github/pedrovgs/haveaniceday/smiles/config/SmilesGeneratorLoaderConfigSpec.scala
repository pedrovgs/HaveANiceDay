package com.github.pedrovgs.haveaniceday.smiles.config

import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

class SmilesGeneratorLoaderConfigSpec extends FlatSpec with Matchers {

  "SmilesGeneratorConfigLoader" should "read the values associated with the smiles generator configuration" in {
    val config = ConfigFactory.load("smilesGenerator/validSmilesGenerator.conf")

    val smilesGeneratorConfig = SmilesGeneratorConfigLoader.loadSmilesGeneratorConfig(config).get

    smilesGeneratorConfig.twitterAccounts shouldBe List("818887507169247232")
    smilesGeneratorConfig.scheduleTasks shouldBe true
    smilesGeneratorConfig.extractionSchedule shouldBe "0 0 8 ? * *"
    smilesGeneratorConfig.generationSchedule shouldBe "0 0 9 ? * *"
    smilesGeneratorConfig.allowManualSmilesExtraction shouldBe true
    smilesGeneratorConfig.allowManualSmilesGeneration shouldBe true
  }

  it should "return none if the configuration does not exist" in {
    val config = ConfigFactory.load("smilesGenerator/emptySmilesGenerator.conf")

    val smilesGeneratorConfig = SmilesGeneratorConfigLoader.loadSmilesGeneratorConfig(config)

    smilesGeneratorConfig shouldBe None
  }

  it should "return none if the configuration does not contain the expected keys" in {
    val config = ConfigFactory.load("smilesGenerator/invalidSmilesGenerator.conf")

    val smilesGeneratorConfig = SmilesGeneratorConfigLoader.loadSmilesGeneratorConfig(config)

    smilesGeneratorConfig shouldBe None
  }
}
