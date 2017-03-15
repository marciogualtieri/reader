# Reader

## Overview

Reader is a simple android app which purpose is showcasing my Scala/Android development skills.

## Developer Notes

I'm putting the steps required to create an android development environment in this section for my own reference.

### Creating an Android Project

I'm using [sbt-android](https://github.com/scala-android/sbt-android) for this purpose.

You can use the plugin not only for build and invoking tests, but also to generate a template project.
You will need to install the plugin globally in this case.

Create the following file:

    ~/.sbt/0.13/plugins/android.sbt

With the following contents:

    addSbtPlugin("org.scala-android" % "sbt-android" % "1.7.6")

Then in a terminal, type the following to create a brand new Android/Scala project:

    gen-android <package name> <project name>

Example:

    $ gen-android com.wire.reader reader

If you have an Android SDK installed, you should have the environment variables `ANDROID_HOME` and `ANDROID_NDK_HOME` 
set in your `.bashrc` file. If you don't, `sbt-android` will download the latest available version for you and install
it under `~/.android/sbt/sdk` as they are needed.

### Basic sbt-android Commands

    $ sbt android:install # deploy app
    $ sbt android:run     # deploy and run app
    $ sbt android:test    # run instrumented and UI tests

### Using an Emulator

#### Install the Emulator

You may use `sdkmanager` to install the emulator. Execute the following command to get a list of what's already 
installed:

    ~/.android/sbt/sdk/tools/bin/sdkmanager --list

You might need to change the command's location accordingly if you have previously installed your Android SDK.

If `emulator` isn't present on the list, execute the following command:

    ~/.android/sbt/sdk/tools/bin/sdkmanager emulator

#### Install Emulator's Dependencies

Google Android APIs:

    ~/.android/sbt/sdk/tools/bin/sdkmanager "system-images;android-24;google_apis;x86_64"

Or (depending on your system):

    ~/.android/sbt/sdk/tools/bin/sdkmanager "system-images;android-24;google_apis;x86"

Android Platform:

    ~/.android/sbt/sdk/tools/bin/sdkmanager "platforms;android-23"

Change according with the platform you want to develop for.

#### Create an Android Virtual Device

    ~/.android/sbt/sdk/tools/bin/avdmanager \
    create avd \
    --package "system-images;android-24;google_apis;x86_64" \
    --tag google_apis \
    --name test

Change according with the platform you need and the name you want for the emulator. In this example, the emulator is
named "test".

#### Start the Emulator

    ~/.android/sbt/sdk/tools/emulator -avg test

You will need to start the emulator in a separated terminal before running Android instrumented and UI tests.

### Macroid

I have chosen [macroid](https://github.com/47deg/macroid) for Android UI development, which consists of a library of 
Scala macros for UI creation.

You will find some documentation on macroid's Scala macros (including examples) 
[here](http://47deg.github.io/macroid/docs/).

After fiddling with a couple of different choices, among them [scaloid](https://github.com/pocorall/scaloid). I feel 
like macroid is the best option at the moment in terms of popularity and compatibility with the latest Android APIs 
(scaloid, it's stronger competitor, only supports the old APIs).

### Testing Setup

Currently, this project supports the following tests:

* Unit Tests (regular [`ScalaTest`](http://www.scalatest.org/) tests using `FunSuite`).
* Instrumented Tests (using [`AndroidTest`](https://developer.android.com/training/testing/start/index.html), which 
require the emulator or an actual Android device).
* UI Tests (using `AndroidTest`, also require emulator or actual device).

To run both instrumented and UI tests, execute the following command:

    sbt android:test
