lazy val commonSettings =
  Def.settings(
    scalaVersion := "2.11.11",
    organization := "com.github.nadavwr",
    publishArtifact in (Compile, packageDoc) := false,
    licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
  )

def commonConfiguration(project: Project): Project =
  project
    .enablePlugins(ScalaNativePlugin)
    .settings(commonSettings)

lazy val pthread =
  (project in file("."))
    .configure(commonConfiguration)
    .settings(
      moduleName := "libpthread-scala-native",
      bintrayVcsUrl := Some("git@github.com:nadavwr/libpthread-scala-native.git")
    )

lazy val unpublished =
  Def.settings(
    publish := {},
    publishLocal := {},
    publishM2 := {}
  )

lazy val plainSample =
  (project in file("plainSample"))
    .configure(commonConfiguration)
    .settings(unpublished)
    .dependsOn(pthread)

lazy val prettySample =
  (project in file("prettySample"))
    .configure(commonConfiguration)
    .settings(unpublished)
    .dependsOn(pthread)
