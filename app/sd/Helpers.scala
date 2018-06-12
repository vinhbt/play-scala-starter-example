package sd

import cats.effect.IO
import doobie._
import doobie.implicits._
import io.circe.{Json, Printer}
import play.api.db.Database
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.mvc.Codec

object Helpers {
  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }

  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = Printer.noSpaces): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }

  implicit class Transaction(val db: Database){
    def withTrans[A](block: ConnectionIO[A]): IO[A] = block.transact(Transactor.fromDataSource[IO](db.dataSource))
  }
}
