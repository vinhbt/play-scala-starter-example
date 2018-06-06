package login

import javax.inject.Inject
import doobie._
import doobie.implicits._
import sd.Helpers._
import services.SDDoobie
import scala.language.implicitConversions

class LoginRes @Inject() (db: SDDoobie){
  import db.xa

  def insert(name: String, bytes: Array[Byte], hash: String): ConnectionIO[Long] =
    sql"""insert into file_info(name, hash, data)
          values($name, $hash, $bytes)""".update.withUniqueGeneratedKeys("id")

  def all: ConnectionIO[List[AuthModel]] = {
    sql"""select id, name, hash, fecha from file_info""".query[AuthModel].to[List]
  }

  def store(name: String, bytes: Array[Byte], hash: String): Long = withTrans {
    insert(name, bytes, hash)
  }.unsafeRunSync()

  def archivos() = withTrans { all }.unsafeRunSync()
}
