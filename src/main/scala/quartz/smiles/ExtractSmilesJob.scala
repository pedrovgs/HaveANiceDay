package quartz.smiles

import com.github.pedrovgs.haveaniceday.smiles.ExtractSmiles
import com.twitter.inject.Logging
import finatra.HaveANiceDayServerMain
import org.quartz.{Job, JobExecutionContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class ExtractSmilesJob extends Job with Logging {

  override def execute(context: JobExecutionContext) = {
    val injector      = HaveANiceDayServerMain.sharedInstance.injector
    val extractSmiles = injector.instance[ExtractSmiles]
    extractSmiles().onComplete {
      case Success(Right(smiles))         => info(s"${smiles.length} smiles extracted properly")
      case Success(Left(extractionError)) => error(s"Error extracting smiles: ${extractionError.message}")
      case Failure(e)                     => error(s"Unhandled exception found during the smiles extraction: ${e.getMessage}")
    }
  }
}
