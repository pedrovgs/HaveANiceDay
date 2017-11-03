package finatra.serializers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import finatra.api.smiles.model.SmileApiModel
import generators.api.smiles._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import specs.TestResources

class SmileApiModelSerializerSpec extends FlatSpec with TestResources with PropertyChecks with Matchers {

  private val mapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
  }

  private val anyTitle           = "Title"
  private val anyId              = 1
  private val anyCompleteSmile   = SmileApiModel(anyId, anyTitle, Some("Message"), Some("http://photos.com/random.png"))
  private val anyIncompleteSmile = SmileApiModel(anyId, anyTitle, None, None)

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

}
