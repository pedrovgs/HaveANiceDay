package finatra.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class RootController extends Controller {

  get("/ping") { _: Request =>
    response.ok
  }
}
