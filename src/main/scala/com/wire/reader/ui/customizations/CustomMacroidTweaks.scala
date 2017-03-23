package com.wire.reader.ui.customizations

import java.text.SimpleDateFormat

import macroid.Tweak

import scala.language.postfixOps

trait CustomMacroidTweaks {

  def timeTweak(timestamp: BigInt): Tweak[TimestampView] =
    Tweak[TimestampView](_.setText(timestampToString(timestamp)))

  private def timestampToString(timestamp: BigInt): String = {
    val df = new SimpleDateFormat()
    df.format(timestamp.toLong)
  }
}

