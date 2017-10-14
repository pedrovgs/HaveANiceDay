package finatra.controllers

import com.github.pedrovgs.haveaniceday.smiles.SmilesGenerator
import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import io.swagger.models.Swagger

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ManualTestingController @Inject()(s: Swagger, smilesGenerator: SmilesGenerator) extends Controller with Logging {
  implicit protected val swagger: Swagger = s

  get("/extractSmiles") { request: Request =>
    if (isLocalhostRequest(request)) {
      info(s"Let's extract some smiles")
      val result = Await.result(smilesGenerator.extractSmiles(), Duration.Inf)
      info(s"Smiles extracted: $result")
      response.ok(result.toString)
    } else {
      response.notFound()
    }
  }

  get("/generateSmiles") { request: Request =>
    if (isLocalhostRequest(request)) {
      info(s"Let's generate some smiles")
      val result = Await.result(smilesGenerator.generateSmiles(), Duration.Inf)
      info(s"Smiles generated: $result")
      response.ok(result.toString)
    } else {
      response.notFound()
    }
  }

  private def isLocalhostRequest(request: Request) = {
    val host = request.host.getOrElse("")
    host.startsWith("localhost")
  }
}
