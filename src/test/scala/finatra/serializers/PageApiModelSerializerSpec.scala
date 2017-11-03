package finatra.serializers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import finatra.api.model.PageApiModel
import generators.common._
import org.scalacheck.Arbitrary._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import specs.TestResources

class PageApiModelSerializerSpec extends FlatSpec with TestResources with PropertyChecks with Matchers {

  private val mapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
  }
  private val anyEmptyPage    = PageApiModel(Seq(), 0, 1, 10)
  private val anyNotEmptyPage = PageApiModel(Seq(1, 2, 3), 3, 1, 10)

  "PageApiModel serializer" should "should serialize an empty page" in {
    val serializedSmile = mapper.readTree(mapper.writeValueAsString(anyEmptyPage))

    val expectedSmile = mapper.readTree(contentFromResource("/api/emptyPage.json"))
    expectedSmile shouldBe serializedSmile
  }

  it should "should serialize a not empty page" in {
    val serializedSmile = mapper.readTree(mapper.writeValueAsString(anyNotEmptyPage))

    val expectedSmile = mapper.readTree(contentFromResource("/api/notEmptyPage.json"))
    expectedSmile shouldBe serializedSmile
  }

  it should "keep the loopback property" in {
    forAll(arbitraryPage(arbitrary[Int])) { page =>
      val serializedPage = mapper.writeValueAsString(page)

      page shouldBe mapper.readValue[PageApiModel[Int]](serializedPage, classOf[PageApiModel[Int]])
    }
  }
}
