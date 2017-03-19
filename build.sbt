scalaVersion := "2.11.8"

enablePlugins(AndroidApp)
android.useSupportVectors

versionCode := Some(1)
version := "0.1-SNAPSHOT"

instrumentTestRunner :=
  "android.support.test.runner.AndroidJUnitRunner"

platformTarget := "android-23"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

val Http4sVersion = "0.15.7a"
libraryDependencies ++=
  "org.scalatest" %% "scalatest" % "3.0.1" % "test" ::
  "com.github.tomakehurst" % "wiremock" % "2.5.1" % "test" ::
  "ch.qos.logback" % "logback-classic"  % "1.2.1" % "test" ::
  "com.android.support.test" % "runner" % "0.5" % "androidTest" ::
  "com.android.support.test.espresso" % "espresso-core" % "2.2.2" % "androidTest" ::
  aar("org.macroid" %% "macroid" % "2.0") ::
  aar("org.macroid" %% "macroid-viewable" % "2.0") ::
  aar("com.android.support" % "support-v4" % "25.0.1") ::
  "org.json4s" %% "json4s-jackson" % "3.5.0" ::
  "org.apache.httpcomponents" % "httpclient" % "4.5.3" ::
  Nil
dependencyOverrides += "org.scala-lang" % "scala-library" % "2.11.8"
testAarWarning := false

proguardScala in Android := true
proguardOptions in Android ++=
  "-ignorewarnings" ::
  "-keep class scala.Dynamic" ::
  "-keepattributes InnerClasses" ::
  "-dontwarn scala.async.internal.**" ::
  "-dontwarn scala.xml.parsing.**" ::
  "-dontwarn org.apache.commons.**" ::
  "-dontwarn org.apache.http.**" ::
  "-dontwarn com.fasterxml.jackson.**" ::
  "-dontwarn com.thoughtworks.paranamer.**" ::
  Nil

packagingOptions in Android :=
  PackagingOptions(excludes =
    Seq("META-INF/NOTICE", "META-INF/NOTICE.txt",
        "META-INF/LICENSE", "META-INF/LICENSE.txt",
        "META-INF/DEPENDENCIES"))