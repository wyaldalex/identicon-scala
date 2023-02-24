name := "Identicon"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.12"

enablePlugins(JavaAppPackaging)

Compile/mainClass := Some("com.tudux.identicon.Main")

libraryDependencies ++= Seq(

  "org.scalatest"     %% "scalatest"                  % "3.2.9",
  "org.slf4j" % "slf4j-simple" % "2.0.5",
  "org.slf4j" % "slf4j-api" % "2.0.5"
)
