package finatra.swagger

import com.google.inject.{Provides, Singleton}
import com.jakehschwartz.finatra.swagger.SwaggerModule
import io.swagger.models.{Info, Swagger}

object HaveANiceDaySwaggerModule extends SwaggerModule {

  @Singleton
  @Provides
  def swagger: Swagger = {
    val swagger = new Swagger()
    val info = new Info()
      .title("Have a nice day API")
      .description(
        "Have a nice API swagger documentation. More information can be found at https://github.com/pedrovgs/haveaniceday.")
      .version("1.0")
    swagger.info(info)
  }
}
