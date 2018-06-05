lazy val coreSettings = Seq(
  coursierParallelDownloads := 16,
  //version := use git describe. see https://github.com/sbt/sbt-git
  git.useGitDescribe := true,
  scalaVersion := V.scala,
  name := "sd-local",
  organization := "com.sandinh",
  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-target:jvm-1.8"),
  daemonUser in Docker := "sd-local",
  daemonUserUid in Docker := Some("82"),
  daemonGroup in Docker := "sd-local",
  daemonGroupGid in Docker := Some("82")
)

// move the args added in play.sbt.PlaySettings.defaultSettings into application.ini
lazy val fixPlayBashSettings = Seq(
  bashScriptExtraDefines ~= { d => d.filterNot(_.contains("-Duser.dir=")) },
  javaOptions in Universal += "-Duser.dir=" + (defaultLinuxInstallLocation in Docker).value
)

def cdnSettings(cs: Configuration*) = cs.map(c =>
  javaOptions in c += ("-Dsd.cdn.version=" + version.value)
)

lazy val playSettings = fixPlayBashSettings ++ cdnSettings(Compile, Universal, Test) ++ Seq(
  // set Dpidfile = /dev/null to prevent error:
  // `This application is already running (Or delete RUNNING_PID file)`
  // http://stackoverflow.com/a/29244028/457612
  javaOptions in Universal += "-Dpidfile.path=/dev/null",
  //TODO remove this after migrated to docker
  javaOptions in Test += "-Dconfig.file=conf/application-test.conf",

  //@see https://www.playframework.com/documentation/2.5.x/Deploying
  sources in (Compile, doc) := Nil,
  publishArtifact in (Compile, packageDoc) := false,
  pipelineStages := Seq(digest, gzip),
  //do not digest files in html & images folders
  excludeFilter in digest := HiddenFileFilter || new SimpleFileFilter(f =>
    Seq("html", "images")
      .map(dir => file("public") / dir)
      .exists { dir => f.getAbsolutePath startsWith dir.getAbsolutePath }
  )
)

lazy val packagerSettings = Seq(
  maintainer := "Gia Bao <giabao@sandinh.net>",
  dockerBaseImage := "openjdk:8-jre-alpine",
  dockerExposedPorts := Seq(9000),
  dockerRepository := Some("r.bennuoc.com/sd"),
  DockerHelper.dockerCommandsSetting
) ++ DockerHelper.mappingsSettings

lazy val `sd-local` = project.in(file("."))
  .enablePlugins(PlayScala, DockerPlugin, AshScriptPlugin, GitVersioning)
  .disablePlugins(PlayFilters)
  .settings(
    coreSettings ++
      D.settings ++
      playSettings ++
      packagerSettings: _*)
