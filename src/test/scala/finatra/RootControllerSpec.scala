package finatra

import com.twitter.finagle.http.Status
import specs.InMemoryDatabaseFeatureTest

class RootControllerSpec extends InMemoryDatabaseFeatureTest {

  test("should return OK at the root path") {
    server.httpGet(path = "/", andExpect = Status.Ok)
  }
}
