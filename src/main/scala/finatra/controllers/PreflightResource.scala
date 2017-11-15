package finatra.controllers


import com.twitter.finagle.mux.Request
import com.twitter.finatra.http.Controller

class PreflightResource extends Controller {

  options("/:*") { _ : Request =>
    response.ok
      .header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "HEAD, GET, PUT, POST, DELETE, OPTIONS")
      .header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization")
      .contentType("application/json")
  }
}