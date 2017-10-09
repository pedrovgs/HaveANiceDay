package finatra

import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class RootControllerSpec extends FeatureTest {

  override val server = new EmbeddedHttpServer(new HaveANiceDayServer)

  test("Should return OK at the root path") {
    server.httpGet(path = "/", andExpect = Status.Ok)
  }
}
