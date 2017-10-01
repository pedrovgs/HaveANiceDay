package generators

import com.github.pedrovgs.haveaniceday.notifications.client.model.FirebaseNotification
import com.github.pedrovgs.haveaniceday.notifications.model.Notification
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen

object notifications {

  val arbitraryNotification: Gen[Notification] = for {
    title    <- arbitrary[String]
    message  <- arbitrary[String]
    photoUrl <- arbitrary[Option[String]]
  } yield Notification(title, message, photoUrl)

  val arbitraryFirebaseNotification: Gen[FirebaseNotification] = for {
    to           <- arbitrary[String]
    notification <- arbitraryNotification
  } yield FirebaseNotification.fromNotification(to, notification)

}
