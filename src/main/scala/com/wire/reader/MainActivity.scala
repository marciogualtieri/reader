package com.wire.reader

import java.util.Properties

import scala.language.postfixOps
import android.os.{Bundle, StrictMode}
import android.widget._
import android.view.ViewGroup.LayoutParams._
import android.view.{Gravity, View}
import android.app.Activity
import android.content.Intent
import macroid._
import macroid.FullDsl._
import com.wire.reader.enums.MainActivityWidgets
import pl.droidsonroids.gif.GifImageView

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

class MainActivity extends Activity with CustomMacroidTweaks with Contexts[Activity] {

  var items: Option[ListView] = slot[ListView]
  var progress: Option[GifImageView] = slot[GifImageView]

  var messages: ListBuffer[Message] = ListBuffer.empty[Message]
  val defaults: Properties = new Properties()

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    val policy = new StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    defaults.load(getAssets.open("reader.properties"))
    val preferences = getSharedPreferences("READER_PREFERENCES", 0)

    def fetching = Future {
      val offset = preferences.getInt("offset", defaults.getProperty("default.offset").toInt)
      val endpoint = preferences.getString("endpoint", defaults.getProperty("default.endpoint"))

      val fetcher = HttpFetcher(endpoint)
      val result = Try(fetcher.messages(offset)) match {
        case Success(content) => content
        case Failure(e) => { Ui.run { dialog(e.getMessage) <~
                                        positiveYes({Ui(true)}) <~
                                        speak
                                     }
                             List.empty[Message] }
      }
      messages ++= result
    }

    val view = l[LinearLayout](

      w[Button] <~
        id(MainActivityWidgets.READ_BUTTON.id) <~
        text("Read") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT)
        <~ On.click {
          (progress <~ show) ~
          (items <~~ Snails.wait(fetching)) ~~
          (items <~ messageListable.listAdapterTweak(messages)) ~~
          (progress <~ hide)
      },

      w[Button] <~
        id(MainActivityWidgets.PREFS_BUTTON.id) <~
        text("Preferences") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT)
        <~ On.click { startActivity(new Intent(this, classOf[PreferencesActivity]))
                      Ui(true) },

      w[Button] <~
        id(MainActivityWidgets.QUIT_BUTTON.id) <~
        text("Quit") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT)
        <~ On.click { finish()
                      Ui(true) },

      w[ListView] <~
        id(MainActivityWidgets.MESSAGE_LIST.id) <~
        wire(items) <~
        FuncOn.itemLongClick[ListView] {
        (adapterView: AdapterView[_], _: View, index: Int, _: Long) =>
          (dialog("Delete this message?") <~
              positiveYes({
                messages -= messageFromListView(adapterView, index)
                (items <~ messageListable.listAdapterTweak(messages.toList)) ~ Ui(true)
              }) <~
              negativeNo(Ui(true)) <~
              speak) ~ Ui(true)
      },

      w[GifImageView] <~
        wire(progress) <~
        Tweak[GifImageView](_.setImageResource(R.drawable.progress)) <~
        hide

    ) <~ (portrait ? vertical | horizontal) <~ Transformer {
      case x: View => x <~ padding(all = 4 dp) } <~
      Tweak[LinearLayout](_.setGravity(Gravity.CENTER_HORIZONTAL))

    setContentView(view.get)
  }

  def messageFromListView(adapterView: AdapterView[_], index: Int): Message =
    adapterView.getItemAtPosition(index).asInstanceOf[Message]
}