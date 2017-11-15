package finatra

import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGeneratorConfig
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import finatra.HaveANiceDayServerMain.sharedInstance
import finatra.api.HaveANiceDayJacksonModule
import finatra.config.ConfigModule
import finatra.controllers.{ManualTestingController, PreflightResource, RootController, SmilesController}
import org.quartz.CronScheduleBuilder._
import org.quartz.JobBuilder._
import org.quartz.Scheduler
import org.quartz.TriggerBuilder._
import org.quartz.impl.StdSchedulerFactory
import quartz.smiles.{ExtractSmilesJob, GenerateSmilesJob}
import slick.SlickModule

object HaveANiceDayServerMain extends HaveANiceDayServer {

  var sharedInstance: HaveANiceDayServer = _

}

class HaveANiceDayServer extends HttpServer {

  override protected def defaultFinatraHttpPort = ":9000"

  override protected def modules = Seq(ConfigModule, SlickModule)

  override protected def jacksonModule = HaveANiceDayJacksonModule

  override protected def configureHttp(router: HttpRouter): Unit =
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[PreflightResource]
      .add[RootController]
      .add[SmilesController]
      .add[ManualTestingController]

  protected override def afterPostWarmup(): Unit = {
    super.afterPostWarmup()
    sharedInstance = this
    configureScheduledTasks()
  }

  private def configureScheduledTasks(): Unit = {
    val scheduler = StdSchedulerFactory.getDefaultScheduler
    val config    = injector.instance[SmilesGeneratorConfig]
    if (config.scheduleTasks) {
      configureExtractSmilesJob(scheduler, config)
      configureGenerateSmilesJob(scheduler, config)
      scheduler.start()
    }
  }

  private def configureExtractSmilesJob(scheduler: Scheduler, config: SmilesGeneratorConfig) = {
    val extractSmilesJob = newJob(classOf[ExtractSmilesJob]).build()
    val schedule         = config.extractionSchedule
    val trigger = newTrigger()
      .withIdentity("SmilesExtractorJob")
      .withSchedule(cronSchedule(schedule))
      .build()
    scheduler.scheduleJob(extractSmilesJob, trigger)
  }

  private def configureGenerateSmilesJob(scheduler: Scheduler, config: SmilesGeneratorConfig) = {
    val generateSmilesJob = newJob(classOf[GenerateSmilesJob]).build()
    val schedule          = config.generationSchedule
    val trigger = newTrigger()
      .withIdentity("SmilesGeneratorJob")
      .withSchedule(cronSchedule(schedule))
      .build()
    scheduler.scheduleJob(generateSmilesJob, trigger)
  }
}
