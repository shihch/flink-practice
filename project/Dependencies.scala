import sbt._

object Dependencies {

  object Versions {
    val flink = "1.4.0"
    val akka = "2.5.9"
    val spark = "2.2.1"
    val json4s = "3.5.3"
  }

  val akka = Seq(
    "com.typesafe.akka" %% "akka-stream" % Versions.akka,
    "com.typesafe.akka" %% "akka-stream-testkit" % Versions.akka % "test",
    "com.typesafe.akka" %% "akka-testkit" % Versions.akka % "test"
  )

  val flink = Seq(
    "org.apache.flink" %% "flink-scala" % Versions.flink,
    "org.apache.flink" %% "flink-streaming-scala" % Versions.flink
  )

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
