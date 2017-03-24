package com.wire.reader.ui.customizations

import android.view.View
import macroid.FullDsl._
import macroid._

import scala.language.postfixOps

trait CustomTransformers {

  def padsAll(size: Int)(implicit ctx: ContextWrapper): Transformer =
    Transformer { case x: View => x <~ padding(all = size dp) }

}


