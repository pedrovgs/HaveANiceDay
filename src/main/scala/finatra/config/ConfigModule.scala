package finatra.config

import com.github.pedrovgs.haveaniceday.notifications.config.FirebaseConfigLoader
import com.github.pedrovgs.haveaniceday.notifications.model.FirebaseConfig
import com.github.pedrovgs.haveaniceday.smiles.config.SmilesGeneratorConfigLoader
import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGeneratorConfig
import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import com.typesafe.config.ConfigFactory

object ConfigModule extends TwitterModule {

  @Singleton
  @Provides
  def firebaseConfig: FirebaseConfig = {
    val config         = ConfigFactory.load("firebase.conf")
    val firebaseConfig = FirebaseConfigLoader.loadFirebaseConfig(config)
    if (firebaseConfig.isDefined) {
      firebaseConfig.get
    } else {
      throw new RuntimeException("Wrong firebase configuration found. Review your firebase.conf file")
    }
  }

  @Singleton
  @Provides
  def smilesGeneratorConfig: SmilesGeneratorConfig = {
    val config                = ConfigFactory.load("smilesGenerator.conf")
    val smilesGeneratorConfig = SmilesGeneratorConfigLoader.loadSmilesGeneratorConfig(config)
    if (smilesGeneratorConfig.isDefined) {
      smilesGeneratorConfig.get
    } else {
      throw new RuntimeException("Wrong smiles generator configuration found. Review your smilesGenerator.conf file")
    }
  }
}
