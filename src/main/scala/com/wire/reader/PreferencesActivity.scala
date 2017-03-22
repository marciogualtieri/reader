package com.wire.reader

import java.util.Properties

import scala.language.postfixOps
import android.os.Bundle
import android.widget._
import android.view.ViewGroup.LayoutParams._
import android.view.View
import android.app.Activity
import android.graphics.Color
import macroid._
import macroid.FullDsl._
import com.wire.reader.ui.enums.PreferencesActivityWidgets
import macroid.contrib.{BgTweaks, TextTweaks}

class PreferencesActivity extends Activity with Contexts[Activity] {

  val defaults: Properties = new Properties()

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    defaults.load(getAssets.open("reader.properties"))

    val preferences = getSharedPreferences("READER_PREFERENCES", 0)
    val endpoint = preferences.getString("endpoint", defaults.getProperty("default.endpoint"))
    val offset = preferences.getInt("offset", defaults.getProperty("default.offset").toInt)

    val view = l[LinearLayout](

      w[TextView] <~ text("Endoint:") <~
        TextTweaks.size(16) <~ TextTweaks.bold,

      w[EditText] <~ id(PreferencesActivityWidgets.ENDPOINT_TEXT.id) <~
        TextTweaks.size(14) <~
        text(endpoint),

      w[TextView] <~ text("Offset:") <~
        TextTweaks.size(16) <~ TextTweaks.bold,

      w[EditText] <~ id(PreferencesActivityWidgets.OFFSET_NUMBER.id) <~
        TextTweaks.size(14) <~
        TextTweaks.numeric <~
        text(offset.toString),

      w[Button] <~
        id(PreferencesActivityWidgets.SAVE_BUTTON.id) <~
        text("Save") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click {
          val endpoint = this.find[EditText](PreferencesActivityWidgets.ENDPOINT_TEXT.id)
          val offset = this.find[EditText](PreferencesActivityWidgets.OFFSET_NUMBER.id)
          val preferences = getSharedPreferences("READER_PREFERENCES", 0)
          val editor = preferences.edit()
          editor.putString("endpoint", endpoint.get.get.getText.toString)
          editor.putInt("offset", offset.get.get.getText.toString.toInt)
          editor.commit()
          finish()
          Ui(true)
        },

      w[Button] <~
        id(PreferencesActivityWidgets.CANCEL_BUTTON.id) <~
        text("Cancel") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click {
          finish()
          Ui(true)
        }

    ) <~ (portrait ? vertical | horizontal) <~ Transformer {
      case x: View => x <~ padding(all = 4 dp)
    }

    setContentView(view.get)
  }

}