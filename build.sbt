import com.typesafe.sbt.packager.docker._

val awsXRayRecorderVersion = "2.0.1"
// Test/Example
val mysqlConnectorJavaVersion = "8.0.13"
val scalikejdbcVersion = "3.3.0"
val scalikejdbcPlayVersion = "2.6.0-scalikejdbc-3.3"
val slf4jApiVersion = "1.7.25"
val scalaLoggingVersion = "3.9.0"

lazy val baseSettings = Seq(
  organization := "net.toqoz",
  version := "0.0.1",
  sbtPlugin := false,
  scalaVersion := "2.12.7",
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xfuture"),
  resolvers ++= Seq(Resolver.sonatypeRepo("releases"), Resolver.sonatypeRepo("snapshots")),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test,
    // java.lang.NoClassDefFoundError: org/scalacheck/Test$TestCallback
    "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
  ),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  parallelExecution in Test := false,
  logBuffered in Test := false
)

lazy val core = (project in file("core"))
  .settings(baseSettings)
  .settings(name := "aws-xray-core")
  .settings(
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-xray-recorder-sdk-core" % awsXRayRecorderVersion % Provided
    )
  )

lazy val play = (project in file("play"))
  .settings(baseSettings)
  .settings(name := "aws-xray-play")
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % _root_.play.core.PlayVersion.current % Provided,
      "com.amazonaws" % "aws-xray-recorder-sdk-core" % awsXRayRecorderVersion % Provided,
      "org.slf4j" % "slf4j-api"  % slf4jApiVersion % Compile,
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion % Compile
    )
  )
  .dependsOn(core)

lazy val jdbc = (project in file("jdbc"))
  .settings(baseSettings)
  .settings(name := "aws-xray-jdbc")
  .settings(
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-xray-recorder-sdk-core" % awsXRayRecorderVersion % Provided,
      "mysql" % "mysql-connector-java" % mysqlConnectorJavaVersion % Test,
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion % Test,
      "org.slf4j" % "slf4j-api"  % slf4jApiVersion % Compile,
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion % Compile
    )
  )
  .dependsOn(core)

lazy val examplePlayScalikeJDBC = (project in file("_example/play-scalikejdbc"))
  .settings(baseSettings)
  .settings(name := "aws-xray-example-play-scalikejdbc")
  .settings(
    libraryDependencies ++= Seq(
      guice,
      "mysql" % "mysql-connector-java" % mysqlConnectorJavaVersion,
      "com.amazonaws" % "aws-xray-recorder-sdk-core" % awsXRayRecorderVersion,
      "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % scalikejdbcPlayVersion,
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
    ),
    dockerEntrypoint := Seq("/opt/docker/bin/entrypoint.sh"),
    dockerCommands := Seq(
      Cmd("FROM", "openjdk:latest"),
      Cmd("WORKDIR", "/opt/docker"),
      ExecCmd("RUN", "apt-get", "update"),
      ExecCmd("RUN", "apt-get", "install", "-y", "mysql-client"),
      Cmd("ADD", "--chown=daemon:daemon", "opt", "/opt"),
      Cmd("USER", "daemon"),
      ExecCmd("RUN", "chmod", "+x", "/opt/docker/bin/entrypoint.sh"),
      ExecCmd("ENTRYPOINT","/opt/docker/bin/entrypoint.sh")
    )
  )
  .enablePlugins(PlayScala, JavaAppPackaging)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(core, play, jdbc)
