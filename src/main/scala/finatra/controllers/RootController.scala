package finatra.controllers

import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging

class RootController @Inject() extends Controller with Logging {

  get("/") { _: Request =>
    response.ok
  }
}
