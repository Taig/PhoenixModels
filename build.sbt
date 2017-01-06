val Scala211 = "2.11.8"

val Scala212 = "2.12.1"

crossScalaVersions :=
    Scala211 ::
    Scala212 ::
    Nil

githubProject := name.value

javacOptions ++= {
    scalaVersion.value match {
        case Scala211 ⇒
            "-source" :: "1.7" ::
            "-target" :: "1.7" ::
            Nil
        case _ => Nil
    }
}

libraryDependencies ++=
    List( "core", "generic" ).map( id ⇒ "io.circe" %% s"circe-$id" % "0.6.1" ) :::
    "org.scalatest" %% "scalatest" % "3.0.1" % "test" ::
    Nil

name := "phoenix-models"

organization := "io.taig"

scalaVersion := Scala212