package finatra

import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGeneratorConfig
import com.jakehschwartz.finatra.swagger.DocsController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import finatra.HaveANiceDayServerMain.sharedInstance
import finatra.config.ConfigModule
import finatra.controllers.RootController
import finatra.swagger.HaveANiceDaySwaggerModule
import io.swagger.models.Swagger
import org.quartz.CronScheduleBuilder._
import org.quartz.JobBuilder._
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder._
import org.quartz.TriggerBuilder._
import org.quartz.impl.StdSchedulerFactory
import quartz.smiles.{ExtractSmilesJob, GenerateSmilesJob}
import slick.SlickModule

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HaveANiceDayServerMain extends HaveANiceDayServer {

  var sharedInstance: HaveANiceDayServer = _

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

  protected override def afterPostWarmup(): Unit = {
    super.afterPostWarmup()
    sharedInstance = this
    configureScheduledTasks()
  }

  private def configureScheduledTasks(): Unit = {
    val scheduler = StdSchedulerFactory.getDefaultScheduler
    val config    = injector.instance[SmilesGeneratorConfig]
    configureExtractSmilesJob(scheduler, config)
    configureGenerateSmilesJob(scheduler, config)
    scheduler.start()
  }

  private def configureExtractSmilesJob(scheduler: Scheduler, config: SmilesGeneratorConfig) = {
    val extractSmilesJob = newJob(classOf[ExtractSmilesJob]).build()
    val intervalInHours  = 24 / config.numberOfExtractionsPerDay
    val trigger = newTrigger()
      .withIdentity("SmilesExtractor")
      .startNow()
      .withSchedule(
        simpleSchedule()
          .withIntervalInHours(intervalInHours)
          .repeatForever())
      .build()
    scheduler.scheduleJob(extractSmilesJob, trigger)
  }

  private def configureGenerateSmilesJob(scheduler: Scheduler, config: SmilesGeneratorConfig) = {
    val generateSmilesJob = newJob(classOf[GenerateSmilesJob]).build()
    val hour              = config.generation24Hour
    val trigger = newTrigger()
      .withIdentity("SmilesGenerator")
      .withSchedule(cronSchedule(s"0 0 $hour ? * *"))
      .build()
    scheduler.scheduleJob(generateSmilesJob, trigger)
  }
}
