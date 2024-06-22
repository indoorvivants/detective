Global / excludeLintKeys += logManager
Global / excludeLintKeys += scalaJSUseMainModuleInitializer
Global / excludeLintKeys += scalaJSLinkerConfig

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    organizationName := "Anton Sviridov",
    organization := "com.indoorvivants.detective",
    sonatypeProfileName := "com.indoorvivants",
    homepage := Some(
      url("https://github.com/indoorvivants/scala-library-template")
    ),
    startYear := Some(2022),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "keynmol",
        "Anton Sviridov",
        "keynmol@gmail.com",
        url("https://blog.indoorvivants.com")
      )
    )
  )
)

organization := "com.indoorvivants.detective"
sonatypeProfileName := "com.indoorvivants"

// https://github.com/cb372/sbt-explicit-dependencies/issues/27
lazy val disableDependencyChecks = Seq(
  unusedCompileDependenciesTest := {},
  missinglinkCheck := {},
  undeclaredCompileDependenciesTest := {}
)

val Scala213 = "2.13.14"
val Scala212 = "2.12.19"
val Scala3 = "3.3.3"
val scalaVersions = Seq(Scala3, Scala212, Scala213)

lazy val munitSettings = Seq(
  libraryDependencies += {
    "org.scalameta" %%% "munit" % "1.0.0" % Test
  },
  testFrameworks += new TestFramework("munit.Framework")
)

lazy val root = project
  .in(file("."))
  .aggregate(platform.projectRefs*)
  .settings(
    noPublishing
  )

lazy val platform = projectMatrix
  .in(file("modules/platform"))
  .settings(
    name := "platform"
  )
  .settings(munitSettings)
  .jvmPlatform(scalaVersions)
  /* .jsPlatform(scalaVersions, disableDependencyChecks) */
  .nativePlatform(scalaVersions, disableDependencyChecks)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoPackage := "com.indoorvivants.detective",
    buildInfoOptions += BuildInfoOption.PackagePrivate,
    buildInfoKeys := Seq[BuildInfoKey](
      version,
      scalaVersion,
      scalaBinaryVersion
    )
    /* scalaJSUseMainModuleInitializer := true, */
    /* scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)) */
  )

/* lazy val docs = projectMatrix */
/*   .in(file("docs")) */
/*   .jvmPlatform(Seq(Scala213)) */
/*   .dependsOn(platform) */
/*   .settings( */
/*     mdocVariables := Map( */
/*       "VERSION" -> version.value */
/*     ), */
/*     mdocIn */
/*     noPublishing */
/*   ) */
/*   .settings(disableDependencyChecks) */
/*   .enablePlugins(MdocPlugin) */

val noPublishing = Seq(
  publish / skip := true,
  publishLocal / skip := true
)

val scalafixRules = Seq(
  "OrganizeImports",
  "DisableSyntax",
  "LeakingImplicitClassVal",
  "NoValInForComprehension"
).mkString(" ")

val CICommands = Seq(
  "clean",
  "compile",
  "test",
  /* "docs/mdoc", */
  "scalafmtCheckAll",
  "scalafmtSbtCheck",
  s"scalafix --check $scalafixRules",
  "headerCheck",
  "undeclaredCompileDependenciesTest",
  "unusedCompileDependenciesTest",
  "missinglinkCheck"
).mkString(";")

val PrepareCICommands = Seq(
  s"scalafix --rules $scalafixRules",
  "scalafmtAll",
  "scalafmtSbt",
  "headerCreate",
  "undeclaredCompileDependenciesTest"
).mkString(";")

addCommandAlias("ci", CICommands)

addCommandAlias("preCI", PrepareCICommands)
