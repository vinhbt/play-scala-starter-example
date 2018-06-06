import play.sbt.PlayImport.{ehcache, jdbc, specs2}
import sbt._
import sbt.Keys._

object V {
  val scala = "2.12.6"
  //play 2.6.13 dependsOn akka 2.5.11
  //but... pls run: sbt evicted / sbt coursierDependencyTree
  val akka = "2.5.12"
  //@note use elastic4s x.y.* if install elasticsearch x.y.* on servers
  val es = "6.1.4"
}

object D {
  val deps = Seq(ehcache, jdbc,
    "sd.pay.card"           %% "core"                     % "5.1.5",
    "com.typesafe.akka"     %% "akka-slf4j"               % V.akka,
    "org.playframework.anorm" %% "anorm"                  % "2.6.2",
    "com.sandinh"           %% "php-utils"                % "1.0.7",
    "io.github.nremond"     %% "pbkdf2-scala"             % "0.6.3",
    "org.reactivecouchbase" %% "reactivecouchbase-rs-core" % "1.2.1",
    "com.dripower"          %% "play-circe"               % "2609.1",
    "org.tpolecat"          %% "doobie-core"              % "0.5.3",
    "org.tpolecat"          %% "doobie-specs2"            % "0.5.3",
  //TODO should we update to v2-rev71-1.23.0?
    "com.google.apis"       %  "google-api-services-androidpublisher" % "v2-rev71-1.21.0",
    "com.sksamuel.elastic4s" %% "elastic4s-core"          % V.es,
    "com.sksamuel.elastic4s" %% "elastic4s-http"          % V.es,
    "com.sksamuel.scrimage" %% "scrimage-core"            % "2.1.8",
    "mysql"                 %  "mysql-connector-java"     % "5.1.46" % Runtime
  )

  val tests = Seq(specs2,
    "org.specs2"            %% "specs2-matcher-extra"     % "3.8.9", //same as in play 2.6.13
    "com.sandinh"           %% "subfolder-evolutions"     % "2.6.7"
  ).map(_ % Test)

  val overrides = Set(
    "com.typesafe.akka"       %% "akka-stream"              % V.akka,
    "xalan"                   % "xalan"                     % "2.7.2",
    "org.scala-lang.modules"  %% "scala-parser-combinators" % "1.0.7",
    //bin compatible with 1.0.6
    "org.scala-lang.modules"  %% "scala-xml"                % "1.1.0",
    "org.scala-lang"          % "scala-reflect"             % V.scala
  )

  val settings = Seq(
    resolvers += "Nexus" at "http://repo.bennuoc.com/repository/maven-public",
    resolvers += Resolver.jcenterRepo,
//    resolvers += Resolver.sonatypeRepo("releases"),
    libraryDependencies ++= deps ++ tests,
    dependencyOverrides ++= overrides.toSeq
  )
}
