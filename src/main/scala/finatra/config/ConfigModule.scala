package finatra.config

import com.github.pedrovgs.haveaniceday.notifications.FirebaseConfigLoader
import com.github.pedrovgs.haveaniceday.notifications.model.FirebaseConfig
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
}
