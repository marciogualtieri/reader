package com.wire.reader.ui

import android.app.Activity
import android.content.{Context, Intent}
import android.os.{Bundle, StrictMode}
import android.view.View
import android.view.ViewGroup.LayoutParams._
import android.widget._
import com.wire.reader.R
import com.wire.reader.entitities.Message
import com.wire.reader.enums.ui.MainActivityWidgets
import com.wire.reader.helpers.{HttpMessageFetcher, JsonMessageSerializer, PreferencesEditor}
import com.wire.reader.ui.customizations.{CustomListables, CustomTransformers}
import macroid.FullDsl.{dialog, wire, _}
import macroid._
import pl.droidsonroids.gif.GifImageView

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

class MainActivity extends Activity with Contexts[Activity]
  with CustomListables
  with CustomTransformers
  with PreferencesEditor {

  val MessagesCacheFile = "messages.json"
  var serializer = JsonMessageSerializer()
  var messages: ListBuffer[Message] = ListBuffer.empty[Message]
  var messageListSlot: Option[ListView] = slot[ListView]
  var progressImageSlot: Option[GifImageView] = slot[GifImageView]

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setPermitAllThreadPolicy()
    messages ++= cachedMessages

    val view = l[LinearLayout](

      w[Button] <~
        id(MainActivityWidgets.READ_BUTTON.id) <~
        text("Read") <~
        layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click {
          (progressImageSlot <~ show) ~
            (messageListSlot <~~ Snails.wait(fetchingFutureTask)) ~~
            (messageListSlot <~ messageListable.listAdapterTweak(messages)) ~~
            (progressImageSlot <~ hide)
        },

      w[Button] <~
        id(MainActivityWidgets.PREFS_BUTTON.id) <~
        text("Preferences") <~
        layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click(
          Ui(startActivity(new Intent(this, classOf[PreferencesActivity])))
        ),

      w[Button] <~
        id(MainActivityWidgets.QUIT_BUTTON.id) <~
        text("Quit") <~ layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT) <~
        On.click(
          Ui(finish())
        ),

      w[ListView] <~
        id(MainActivityWidgets.MESSAGE_LIST.id) <~
        wire(messageListSlot) <~
        messageListable.listAdapterTweak(messages) <~
        (FuncOn itemLongClick[ListView] {
          (adapterView: AdapterView[_], _: View, index: Int, _: Long) =>
            (dialog("Delete Message?") <~
              positiveYes(deleteMessageTask(adapterView, index)) <~
              negativeNo(Ui(true)) <~
              speak) ~ Ui(true)
        }),

      w[GifImageView] <~
        wire(progressImageSlot) <~
        gifImageTweak(R.drawable.progress) <~
        hide

    ) <~
      padsAll(4) <~
      orientedLinearLayoutTweak <~
      alignedLinearLayoutTweek

    setContentView(view.get)
  }

  override def onStop(): Unit = {
    super.onStop()
    cacheMessages(messages.toList)
  }

  private def fetchingFutureTask: Future[Unit] = Future {
    Try(fetchedMessages) match {
      case Success(ms) =>
        messages ++= ms
        if(ms.nonEmpty) saveOffsetPreference(ms.last.index + 1)
      case Failure(e) => Ui.run(dialog(e.getMessage) <~ positiveYes({Ui(true)}) <~ speak)
    }
  }

  private def deleteMessageTask(adapterView: AdapterView[_], index: Int) = {
    messages -= messageFromListable(adapterView, index)
    messageListSlot <~ messageListable.listAdapterTweak(messages)
  }

  private def cacheMessages(messages: List[Message]): Unit = {
    val file = openFileOutput(MessagesCacheFile, Context.MODE_PRIVATE)
    Try(file.write(serializer.composeMessages(messages).getBytes)) match {
      case Success(_) => file.close()
      case Failure(e) => file.close(); throw e
    }
  }

  private def cachedMessages: List[Message] =
    Try(openFileInput(MessagesCacheFile)) match {
      case Success(stream) =>
        serializer.parseMessages(Source.fromInputStream(stream).mkString)
      case Failure(_) => List.empty[Message]
    }

  private def fetchedMessages: List[Message] = {
    val fetcher = HttpMessageFetcher(endpointPreferenceValue)
    fetcher.fetchedMessages(offsetPreferenceValue)
  }

  private def setPermitAllThreadPolicy(): Unit = {
    val policy = new StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
  }
}