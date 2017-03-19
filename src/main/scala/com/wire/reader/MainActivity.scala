package com.wire.reader

import scala.language.postfixOps
import android.os.{Bundle, StrictMode}
import android.widget._
import android.view.ViewGroup.LayoutParams._
import android.view.View
import android.app.Activity
import macroid._
import macroid.FullDsl._

import scala.collection.mutable.ListBuffer

object MainActivityWidgets extends Enumeration {
  val READ_BUTTON, QUIT_BUTTON, MESSAGE_LIST = Value
}

class MainActivity extends Activity with CustomMacroidTweaks with Contexts[Activity] {

  var items: Option[ListView] = slot[ListView]
  var messages: ListBuffer[Message] = ListBuffer.empty[Message]

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    val policy = new StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    val endpoint = HttpFetcher("https", "rawgit.com", 443, "/wireapp/android_test_app/master/endpoint")
    val offset = 9

    val view = l[LinearLayout](

      w[Button] <~
        id(MainActivityWidgets.READ_BUTTON.id) <~
        text("Read") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT)
        <~ On.click { messages ++= endpoint.messages(offset)
        (items <~ messageListable.listAdapterTweak(messages)) ~ Ui(true) },

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
          (dialog("Are you sure?") <~
              positiveYes({
                messages -= messageFromListView(adapterView, index)
                (items <~ messageListable.listAdapterTweak(messages.toList)) ~ Ui(true)
              }) <~
              negativeNo(Ui(true)) <~
              speak) ~ Ui(true)
      }

    ) <~ (portrait ? vertical | horizontal) <~ Transformer {
      case x: View => x <~ padding(all = 4 dp) }

    setContentView(view.get)
  }

  def messageFromListView(adapterView: AdapterView[_], index: Int): Message =
    adapterView.getItemAtPosition(index).asInstanceOf[Message]
}