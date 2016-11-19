name := """follower-maze"""

version := "1.0"

scalaVersion := "2.11.7"

val akkaVer = "2.4.0"

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.4",
  "com.typesafe.akka" %% "akka-testkit" % akkaVer % "test"
)

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
