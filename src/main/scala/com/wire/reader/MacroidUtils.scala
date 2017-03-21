package com.wire.reader

import java.text.SimpleDateFormat

import android.content.{Context, Intent}
import android.graphics.Color
import android.widget._
import macroid.FullDsl._
import macroid._
import macroid.contrib.Layouts.VerticalLinearLayout
import macroid.contrib.TextTweaks
import macroid.viewable.Listable

import scala.language.postfixOps





trait CustomMacroidTweaks {

  def messageListable(implicit ctx: ContextWrapper): Listable[Message, VerticalLinearLayout] =
    Listable[Message].tr {
      l[VerticalLinearLayout](
        w[CustomIdView] <~ TextTweaks.size(12) <~ TextTweaks.color(Color.parseColor("blue")),
        w[TextView] <~ TextTweaks.size(16),
        w[CustomTimeView] <~ TextTweaks.size(12) <~ TextTweaks.color(Color.parseColor("green"))
      )
    }(message => Transformer {
      case timeView: CustomTimeView => timeView <~ timeTweak(message.time)
      case idView: CustomIdView => idView <~ text(message.id)
      case textView: TextView => textView <~ text(message.text)
    })

  private def timestampToString(timestamp: BigInt): String = {
    val df = new SimpleDateFormat()
    df.format(timestamp.toLong)
  }

  private def timeTweak(timestamp: BigInt): Tweak[CustomTimeView] =
    Tweak[CustomTimeView](_.setText(timestampToString(timestamp)))
}

