package services

import cats.effect.IO
import javax.inject.{Inject, Singleton}
import play.api.db.Database
import doobie.util.transactor.Transactor

@Singleton
class SDDoobie @Inject()(db: Database) {
  implicit lazy val xa = Transactor.fromDataSource[IO](db.dataSource)
}
