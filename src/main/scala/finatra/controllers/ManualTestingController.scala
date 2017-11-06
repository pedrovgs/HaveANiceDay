package finatra.controllers

import com.github.pedrovgs.haveaniceday.smiles.SmilesGenerator
import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGeneratorConfig
import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ManualTestingController @Inject()(config: SmilesGeneratorConfig, smilesGenerator: SmilesGenerator)
    extends Controller
    with Logging {

  get("/extractSmiles") { request: Request =>
    if (config.allowManualSmilesExtraction) {
      info(s"Let's extract some smiles")
      val result = Await.result(smilesGenerator.extractSmiles(), Duration.Inf)
      info(s"Smiles extracted: $result")
      response.ok(result.toString)
    } else {
      response.notFound()
    }
  }

  get("/generateSmiles") { request: Request =>
    if (config.allowManualSmilesGeneration) {
      info(s"Let's generate some smiles")
      val result = Await.result(smilesGenerator.generateSmiles(), Duration.Inf)
      info(s"Smiles generated: $result")
      response.ok(result.toString)
    } else {
      response.notFound()
    }
  }
}
