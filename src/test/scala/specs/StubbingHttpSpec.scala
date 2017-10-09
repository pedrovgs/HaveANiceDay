package specs

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.scalatest.{BeforeAndAfter, FlatSpec}

object StubbingHttpSpec {
  val port: Int       = 8080
  val host: String    = "localhost"
  val baseUrl: String = "http://" + host + ":" + port
}

abstract class StubbingHttpSpec extends FlatSpec with BeforeAndAfter {

  import StubbingHttpSpec._

  val wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(port))

  before {
    wireMockServer.start()
    WireMock.configureFor(host, port)
  }

  after {
    wireMockServer.stop()
  }
}
