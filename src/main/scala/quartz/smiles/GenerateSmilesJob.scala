package quartz.smiles

import com.github.pedrovgs.haveaniceday.smiles.GenerateSmiles
import com.twitter.inject.Logging
import finatra.HaveANiceDayServerMain
import org.quartz.{Job, JobExecutionContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class GenerateSmilesJob extends Job with Logging {

  override def execute(context: JobExecutionContext) = {
    val injector       = HaveANiceDayServerMain.sharedInstance.injector
    val generateSmiles = injector.instance[GenerateSmiles]
    generateSmiles().onComplete {
      case Success(Right(smile))          => info(s"Smile generated properly 😃: $smile")
      case Success(Left(generationError)) => error(s"Error generating smiles: ${generationError.message}")
      case Failure(e)                     => error(s"Unhandled exception found during the smiles generation: ${e.getMessage}")
    }
  }
}
