scalaVersion := "2.11.8"

enablePlugins(AndroidApp)
android.useSupportVectors

versionCode := Some(1)
version := "0.1-SNAPSHOT"

instrumentTestRunner :=
  "android.support.test.runner.AndroidJUnitRunner"

platformTarget := "android-23"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

libraryDependencies ++=
  "com.android.support.test" % "runner" % "0.5" % "androidTest" ::
  "com.android.support.test.espresso" % "espresso-core" % "2.2.2" % "androidTest" ::
  aar("org.macroid" %% "macroid" % "2.0") ::
  aar("org.macroid" %% "macroid-viewable" % "2.0") ::
  aar("com.android.support" % "support-v4" % "25.0.1") ::
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
  Nil
