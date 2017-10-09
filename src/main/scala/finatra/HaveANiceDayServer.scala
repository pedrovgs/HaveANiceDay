package finatra

import com.jakehschwartz.finatra.swagger.DocsController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import finatra.config.ConfigModule
import finatra.controllers.{NotificationsController, RootController}
import finatra.swagger.HaveANiceDaySwaggerModule
import io.swagger.models.Swagger
import slick.SlickModule

object HaveANiceDayServerMain extends HaveANiceDayServer

object HaveANiceDaySwagger extends Swagger

class HaveANiceDayServer extends HttpServer {

  override protected def defaultFinatraHttpPort = ":9000"

  override protected def modules = Seq(HaveANiceDaySwaggerModule, ConfigModule, SlickModule)

  override protected def configureHttp(router: HttpRouter): Unit =
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[DocsController]
      .add[RootController]
      .add[NotificationsController]
}
