package com.wire.reader.ui.customizations

import java.text.SimpleDateFormat

import android.view.Gravity
import android.widget.LinearLayout
import macroid.FullDsl._
import macroid._
import pl.droidsonroids.gif.GifImageView

import scala.language.postfixOps

trait CustomTweaks {

  def timeTweak(timestamp: BigInt): Tweak[TimestampView] =
    Tweak[TimestampView](_.setText(timestampToString(timestamp)))

  def gifImageTweak(resId: Int): Tweak[GifImageView] =
    Tweak[GifImageView](_.setImageResource(resId))

  def alignedLinearLayoutTweek: Tweak[LinearLayout] =
    Tweak[LinearLayout](_.setGravity(Gravity.CENTER_HORIZONTAL))

  def orientedLinearLayoutTweak(implicit ctx: ContextWrapper): Tweak[LinearLayout] =
    portrait ? vertical | horizontal

  private def timestampToString(timestamp: BigInt): String = {
    val df = new SimpleDateFormat()
    df.format(timestamp.toLong)
  }
}

