import sbt.{Def, _}
import sbt.Keys._
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.docker._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.Docker

case class MultiRunCmd(cmds: String*) extends CmdLike {
  def makeContent = cmds.mkString("RUN \\\n  ", " && \\\n  ", "\n")
}

object DockerHelper {
  private val defaultJavaOpts = Seq("-Xms512M", "-Xmx1024M")

  val mappingsSettings = inConfig(Docker)(Seq(
    mappings := {
      val baseDir = defaultLinuxInstallLocation.value
      val excludePaths = List(
        "README.md",
        s"bin/${name.value}.bat",
        "conf/application-prod.conf",
        "conf/application-test.conf"
      ).map(f => baseDir + "/" + f)

      def layerPath(p: String): String = {
        val shouldPlaceIn2ndLayer = List(
          "/bin/", "/conf/", s"/lib/${organization.value}.${name.value}-"
        ).exists(p.contains)
        if (shouldPlaceIn2ndLayer) baseDir + "-2" + p.substring(baseDir.length)
        else p
      }

      def warn(msg: String) = streams.value.log.warn(msg)

      mappings.value.filterNot { case (_, p) =>
        if (excludePaths.contains(p)) {
          warn(s"docker - excluding $p")
          true
        } else false
      }.map {
        case (f, p) => f -> layerPath(p)
      }
    }
  ))
  val dockerCommandsSetting: Def.Setting[Task[Seq[CmdLike]]] = dockerCommands in Docker := {
    val dockerBaseDirectory = (defaultLinuxInstallLocation in Docker).value
    val entry = dockerEntrypoint.value
    val user = (daemonUser in Docker).value

    val cmdAdds = {
      val dir = dockerBaseDirectory.dropWhile(_ == '/')
      Seq(
        Cmd("ADD", s"$dir /$dir"),
        Cmd("ADD", s"$dir-2 /$dir")
      )
    }

    val runCmd = {
      val gId = (daemonGroupGid in Docker).value.getOrElse("82")
      val uId = (daemonUserUid in Docker).value.getOrElse("82")
      val group = (daemonGroup in Docker).value
      MultiRunCmd(
        s"addgroup -g $gId -S $group",
        s"adduser -u $uId -D -S -G $group $user",
        s"chown -R $user:$group bin"
//        """sed 's|^java \(.*$opts \)|java \1$JAVA_OPTS |' -i """ + entry.head
      )
    }

    val exposedCmd = {
      val ports = dockerExposedPorts.value
      if (ports.isEmpty) None
      else Some(Cmd("EXPOSE", ports mkString " "))
    }

    Seq(
      Cmd("FROM", dockerBaseImage.value),
      Cmd("LABEL", "maintainer=\"" + maintainer.value + "\"")
    ) ++ cmdAdds ++ Seq(
      Cmd("WORKDIR", dockerBaseDirectory),
      runCmd,
      Cmd("ENV", "JAVA_OPTS", defaultJavaOpts.mkString("\"", " ", "\""))) ++
      exposedCmd ++ //not have VOLUME cmd
      Seq(
        Cmd("USER", user),
        // FIXME catch SIGHUP
        ExecCmd("ENTRYPOINT", entry: _*)
      )
  }
}
