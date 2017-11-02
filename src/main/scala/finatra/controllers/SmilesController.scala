package finatra.controllers

import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging

class SmilesController @Inject() extends Controller with Logging {

  get("/api/smiles") { _: Request =>
    response.ok
  }

  get("/api/smile/:id") { request: Request =>
    val smileId = request.params("id")
    response.ok
  }

  get("/api/randomSmile") { _: Request =>
    response.ok
  }
}
