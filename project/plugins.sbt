addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.4")
// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.15")

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt-coursier" % "1.15")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

// https://github.com/coursier/coursier/issues/450
classpathTypes += "maven-plugin"

