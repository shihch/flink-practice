import sbt._

object Dependencies {

  object Versions {
    val flink = "1.4.0"
    val akka = "2.5.9"
    val spark = "2.2.1"
    val json4s = "3.5.3"
  }

  val circeVersion = "0.9.1"
  val circe=Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)

  val logback=Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3" % Test
  )

  val scalatest=Seq(
    "junit" % "junit" % "4.12" % Test,
    "org.scalactic" %% "scalactic" % "3.0.5",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )

  val akka = Seq(
    "com.typesafe.akka" %% "akka-stream" % Versions.akka,
    "com.typesafe.akka" %% "akka-stream-testkit" % Versions.akka % "test",
    "com.typesafe.akka" %% "akka-testkit" % Versions.akka % "test"
  )

  val flink = Seq(
    "org.apache.flink" %% "flink-scala" % Versions.flink,
    "org.apache.flink" %% "flink-streaming-scala" % Versions.flink
  ) ++ circe ++ logback ++ scalatest

  val spark = Seq(
    "org.apache.spark" %% "spark-core" % Versions.spark,
    "org.apache.spark" %% "spark-sql" % Versions.spark,
    "org.apache.spark" %% "spark-streaming" % Versions.spark
  )

  val json4s = Seq(
    "org.json4s" %% "json4s-native" % Versions.json4s,
    "org.json4s" %% "json4s-jackson" % Versions.json4s
  )
}
