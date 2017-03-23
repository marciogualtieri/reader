package com.wire.reader.ui

import java.util.Properties

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams._
import android.widget._
import com.wire.reader.constants.ReaderPreferences
import com.wire.reader.constants.ReaderProperties
import com.wire.reader.enums.ui.PreferencesActivityWidgets
import macroid.FullDsl._
import macroid._
import macroid.contrib.TextTweaks

import scala.language.postfixOps

class PreferencesActivity extends Activity with Contexts[Activity] {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    val view = l[LinearLayout](

      w[TextView] <~ text("Endoint:") <~
        TextTweaks.size(16) <~ TextTweaks.bold,

      w[EditText] <~ id(PreferencesActivityWidgets.ENDPOINT_TEXT.id) <~
        TextTweaks.size(14) <~
        text(endpointPreferenceValue),

      w[TextView] <~ text("OFFSET:") <~
        TextTweaks.size(16) <~ TextTweaks.bold,

      w[EditText] <~ id(PreferencesActivityWidgets.OFFSET_NUMBER.id) <~
        TextTweaks.size(14) <~
        TextTweaks.numeric <~
        text(offsetPreferenceValue.toString),

      w[Button] <~
        id(PreferencesActivityWidgets.SAVE_BUTTON.id) <~
        text("Save") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click(
          Ui {
            savePreferences()
            finish()
          }
        ),

      w[Button] <~
        id(PreferencesActivityWidgets.CANCEL_BUTTON.id) <~
        text("Cancel") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click(
          Ui {
            finish()
          }
        )

    ) <~ (portrait ? vertical | horizontal) <~ Transformer {
      case x: View => x <~ padding(all = 4 dp)
    }

    setContentView(view.get)
  }

  private def endpointWidgetValue: String =
    this.find[EditText](PreferencesActivityWidgets.ENDPOINT_TEXT.id)
      .get.get.getText.toString

  private def offsetWidgetValue: Int =
    this.find[EditText](PreferencesActivityWidgets.OFFSET_NUMBER.id)
      .get.get.getText.toString.toInt

  private def savePreferences(): Unit = {
    val preferences = getSharedPreferences("READER_PREFERENCES", 0)
    val editor = preferences.edit()
    editor.putString(ReaderPreferences.ENDPOINT, endpointWidgetValue)
    editor.putInt(ReaderPreferences.OFFSET, offsetWidgetValue)
    editor.commit()
  }

  private def endpointPreferenceValue: String =
    preferences.getString(
      ReaderPreferences.ENDPOINT,
      defaults.getProperty(ReaderProperties.DefaultEndpoint)
    )

  private def offsetPreferenceValue: Int =
    preferences.getInt(
      ReaderPreferences.OFFSET,
      defaults.getProperty(ReaderProperties.DefaultOffset).toInt
    )

  private def defaults: Properties = {
    val properties: Properties = new Properties()
    properties.load(getAssets.open("reader.properties"))
    properties
  }

  private def preferences: SharedPreferences =
    getSharedPreferences("READER_PREFERENCES", 0)

}