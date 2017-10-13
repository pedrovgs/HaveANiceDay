package quartz.smiles

import com.github.pedrovgs.haveaniceday.smiles.SmilesGenerator
import com.twitter.inject.Logging
import finatra.HaveANiceDayServerMain
import org.quartz.{Job, JobExecutionContext}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class ExtractSmilesJob extends Job with Logging {

  override def execute(context: JobExecutionContext) = {
    val injector        = HaveANiceDayServerMain.sharedInstance.injector
    val smilesGenerator = injector.instance[SmilesGenerator]
    smilesGenerator.extractSmiles().onComplete {
      case Success(Right(smiles))         => info(s"${smiles.length} smiles extracted properly 😃")
      case Success(Left(extractionError)) => error(s"Error extracting smiles: ${extractionError.message}")
      case Failure(e)                     => error(s"Unhandled exception found during the smiles extraction: ${e.getMessage}")
    }
  }
}
