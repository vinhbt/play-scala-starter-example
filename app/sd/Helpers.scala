package sd

import cats.effect.IO
import doobie._
import doobie.implicits._

object Helpers {
  implicit def withTrans[A](block: ConnectionIO[A])(implicit xa: Transactor[IO]): IO[A] = block.transact(xa)
}
