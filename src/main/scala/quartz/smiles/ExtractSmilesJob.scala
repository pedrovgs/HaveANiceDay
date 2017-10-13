package quartz.smiles

import org.quartz.{Job, JobExecutionContext}

class ExtractSmilesJob extends Job {

  override def execute(context: JobExecutionContext) = {
    //TODO: We will need the injector here because we can't use the constructor
    println("-------------------------> whatever")
  }
}
