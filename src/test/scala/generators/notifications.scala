package generators

import generators.common._
import com.github.pedrovgs.haveaniceday.notifications.client.model.FirebaseNotification
import com.github.pedrovgs.haveaniceday.notifications.model.Notification
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen

object notifications {

  val arbitraryNotification: Gen[Notification] = for {
    id       <- Gen.posNum[Long]
    title    <- arbitraryStrBetweenSize(1, 60)
    message  <- arbitraryStrMaxSize(280)
    photoUrl <- arbitrary[Option[String]]
  } yield Notification(id, title, message, photoUrl)

  val arbitraryFirebaseNotification: Gen[FirebaseNotification] = for {
    to           <- arbitrary[String]
    notification <- arbitraryNotification
  } yield FirebaseNotification.fromNotification(to, notification)

}
