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
import org.quartz.impl.StdSchedulerFactory
import org.quartz.TriggerBuilder._
import org.quartz.SimpleScheduleBuilder._
import org.quartz.JobBuilder._
import quartz.smiles.ExtractSmilesJob

object HaveANiceDayServerMain extends HaveANiceDayServer {
  protected override def postInjectorStartup(): Unit = {
    val scheduler        = StdSchedulerFactory.getDefaultScheduler
    val extractSmilesJob = newJob(classOf[ExtractSmilesJob]).build()
    val trigger = newTrigger()
      .withIdentity("Once a day", "HaveANiceDay")
      .startNow()
      .withSchedule(
        simpleSchedule()
          .withIntervalInSeconds(1) //TODO: Get this value from the configuration
          .repeatForever())
      .build()
    scheduler.scheduleJob(extractSmilesJob, trigger)
    scheduler.start()
  }
}

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
