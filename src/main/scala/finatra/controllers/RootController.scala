package finatra.controllers

import com.google.inject.Inject
import com.jakehschwartz.finatra.swagger.SwaggerController
import com.twitter.finagle.http.Request
import io.swagger.models.Swagger

class RootController @Inject()(s: Swagger) extends SwaggerController {
  implicit protected val swagger: Swagger = s

  getWithDoc("/") { o =>
    o.tag("Root")
      .summary("Root endpoint created to simply return 200 status code if the service is running.")
      .responseWith(200)
  } { _: Request =>
    response.ok
  }
}
