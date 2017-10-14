package specs

import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import finatra.HaveANiceDayServer
import slick.Database

abstract class InMemoryDatabaseFeatureTest extends FeatureTest with InMemoryDatabase {

  override val server = new EmbeddedHttpServer(new HaveANiceDayServer)
    .bind[Database](database)

}
