package com.wire.reader

import java.text.SimpleDateFormat

import android.graphics.Color
import macroid.FullDsl._
import macroid._
import macroid.contrib.Layouts.VerticalLinearLayout
import macroid.contrib.TextTweaks
import macroid.viewable.Listable

import scala.language.postfixOps
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView

case class CustomTimeView(context: Context) extends TextView(context)

case class CustomIdView(context: Context) extends TextView(context)

trait CustomMacroidTweaks {

  def messageListable(implicit ctx: ContextWrapper): Listable[Message, VerticalLinearLayout] =
    Listable[Message].tr {
      l[VerticalLinearLayout](
        w[SimpleDraweeView],
        w[CustomIdView] <~ TextTweaks.size(12) <~ TextTweaks.color(Color.parseColor("blue")),
        w[TextView] <~ TextTweaks.size(16),
        w[CustomTimeView] <~ TextTweaks.size(12) <~ TextTweaks.color(Color.parseColor("green"))
      )
    }(message => Transformer {
      case timeView: CustomTimeView => timeView <~ timeTweak(message.time)
      case idView: CustomIdView => idView <~ text(message.id)
      case textView: TextView => textView <~ text(message.text)
      case draweeView: SimpleDraweeView => draweeView <~ draweeTweak(message.text)
    })

  private def timestampToString(timestamp: BigInt): String = {
    val df = new SimpleDateFormat()
    df.format(timestamp.toLong)
  }

  private def timeTweak(timestamp: BigInt): Tweak[CustomTimeView] =
    Tweak[CustomTimeView](_.setText(timestampToString(timestamp)))

  private def draweeTweak(text: String): Tweak[SimpleDraweeView] = {
    if(isLink(text)) Tweak[SimpleDraweeView](_.setImageURI(Uri.parse(text)))
    else Tweak.blank
  }

  private def isLink(s: String): Boolean = {
    val pattern = "^https?://\\S+$".r
    pattern.pattern.matcher(s).matches
  }
}

