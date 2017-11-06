package finatra.serializers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import finatra.api.model.PageApiModel
import finatra.api.smiles.model.SmileApiModel
import generators.api.smiles._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import specs.{JsonParser, TestResources}

class SmileApiModelSerializerSpec
    extends FlatSpec
    with TestResources
    with PropertyChecks
    with Matchers
    with JsonParser {

  private val anyTitle           = "Title"
  private val anyId              = 1
  private val anyCompleteSmile   = SmileApiModel(anyId, anyTitle, Some("Message"), Some("http://photos.com/random.png"))
  private val anyIncompleteSmile = SmileApiModel(anyId, anyTitle, None, None)
  private val anyPage            = PageApiModel(Seq(anyCompleteSmile, anyIncompleteSmile), 10, 1, 5)

  "SmileApiModel serializer" should "should serialize a complete SmileApiModel instance into json" in {
    val serializedSmile = mapper.readTree(mapper.writeValueAsString(anyCompleteSmile))

    val expectedSmile = mapper.readTree(contentFromResource("/api/smiles/completeSmile.json"))
    expectedSmile shouldBe serializedSmile
  }

  it should "serialize an incomplete SmileApiModel instance into json" in {
    val serializedSmile = mapper.readTree(mapper.writeValueAsString(anyIncompleteSmile))

    val expectedSmile = mapper.readTree(contentFromResource("/api/smiles/incompleteSmile.json"))
    expectedSmile shouldBe serializedSmile
  }

  it should "keep the loopback property" in {
    forAll(arbitrarySmileApiModel) { smile =>
      val serializedSmile = mapper.writeValueAsString(smile)

      val deserializedSmile = mapper.readValue[SmileApiModel](serializedSmile, classOf[SmileApiModel])

      smile shouldBe deserializedSmile
    }
  }

  "PageApiModel for smiles" should "serialize a list of smiles" in {
    val serializedPage = mapper.readTree(mapper.writeValueAsString(anyPage))

    val expectedPage = mapper.readTree(contentFromResource("/api/smiles/smilesPage.json"))

    expectedPage shouldBe serializedPage
  }

}
