package finatra.api

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.twitter.finatra.json.modules.FinatraJacksonModule

object HaveANiceDayJacksonModule extends FinatraJacksonModule {

  override val propertyNamingStrategy: PropertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE

}
