lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)

name := "lm2-rank"
organization := "com.lordmancer2"
version := "1.0"
scalaVersion := "2.11.11"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies ++= {
  val akkaV = "2.4.19"
  val jodaV = "2.8.9"
  val akkaHttpV = "10.0.9"
//  val elastic4sV = "5.4.5"
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jodaV,
    "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % jodaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
//    "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sV,
//    "com.sksamuel.elastic4s" %% "elastic4s-spray-json" % elastic4sV,
    "net.cakesolutions" %% "scala-kafka-client-akka" % "0.9.0.0"
//    "net.cakesolutions" %% "scala-kafka-client-testkit" % "0.9.0.0" % "test"
  )
}
