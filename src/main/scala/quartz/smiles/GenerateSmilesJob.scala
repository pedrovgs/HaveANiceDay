package quartz.smiles

import com.github.pedrovgs.haveaniceday.smiles.SmilesGenerator
import com.twitter.inject.Logging
import finatra.HaveANiceDayServerMain
import org.quartz.{Job, JobExecutionContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class GenerateSmilesJob extends Job with Logging {

  override def execute(context: JobExecutionContext) = {
    val injector        = HaveANiceDayServerMain.sharedInstance.injector
    val smilesGenerator = injector.instance[SmilesGenerator]
    ???
  }
}
