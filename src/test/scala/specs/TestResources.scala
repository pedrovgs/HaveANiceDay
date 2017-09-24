package specs

import scala.io.Source

trait TestResources {

  def contentFromResource(resourceName: String): String = {
    Source.fromURL(getClass.getResource(resourceName)).getLines().mkString("\n")
  }
}
