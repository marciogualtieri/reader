package com.wire.reader.ui

import android.app.Activity

import android.os.Bundle
import android.view.ViewGroup.LayoutParams._
import android.widget._
import com.wire.reader.enums.ui.PreferencesActivityWidgets
import com.wire.reader.helpers.PreferencesEditor
import com.wire.reader.ui.customizations.{CustomTransformers, CustomTweaks}
import macroid.FullDsl._
import macroid._
import macroid.contrib.TextTweaks

import scala.language.postfixOps

class PreferencesActivity extends Activity with Contexts[Activity]
  with CustomTweaks
  with CustomTransformers
  with PreferencesEditor {

  val TextLabelSize = 16
  val TextEditSize = 14
  val PaddingSize = 4

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    val view = l[LinearLayout](

      w[TextView] <~ text("Endpoint:") <~
        TextTweaks.size(TextLabelSize) <~ TextTweaks.bold,

      w[EditText] <~
        id(PreferencesActivityWidgets.ENDPOINT_TEXT.id) <~
        TextTweaks.size(TextEditSize) <~
        text(endpointPreferenceValue),

      w[TextView] <~ text("Offset:") <~
        TextTweaks.size(TextLabelSize) <~ TextTweaks.bold,

      w[EditText] <~
        id(PreferencesActivityWidgets.OFFSET_NUMBER.id) <~
        TextTweaks.size(TextEditSize) <~
        TextTweaks.numeric <~
        text(offsetPreferenceValue.toString),

      w[Button] <~
        id(PreferencesActivityWidgets.SAVE_BUTTON.id) <~
        text("Save") <~
        layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click(
          Ui {
            saveEndpointPreference(endpointWidgetValue)
            saveOffsetPreference(offsetWidgetValue)
            finish()
          }
        ),

      w[Button] <~
        id(PreferencesActivityWidgets.CANCEL_BUTTON.id) <~
        text("Cancel") <~
        layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click(
          Ui(finish())
        )

    ) <~
      orientedLinearLayoutTweak <~
      padsAll(PaddingSize)

    setContentView(view.get)
  }

  private def endpointWidgetValue: String =
    this.find[EditText](PreferencesActivityWidgets.ENDPOINT_TEXT.id)
      .get.get.getText.toString

  private def offsetWidgetValue: Int =
    this.find[EditText](PreferencesActivityWidgets.OFFSET_NUMBER.id)
      .get.get.getText.toString.toInt
}
