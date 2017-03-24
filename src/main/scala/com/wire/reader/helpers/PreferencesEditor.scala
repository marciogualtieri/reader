package com.wire.reader.helpers

import java.util.Properties

import android.app.Activity
import android.content.SharedPreferences

import com.wire.reader.constants.ReaderPreferences
import com.wire.reader.constants.ReaderProperties

import scala.language.postfixOps

trait PreferencesEditor extends Activity {

  def saveEndpointPreference(endpoint: String): Unit = {
    val preferences = getSharedPreferences("READER_PREFERENCES", 0)
    val editor = preferences.edit()
    editor.putString(ReaderPreferences.ENDPOINT, endpoint)
    editor.commit()
  }

  def saveOffsetPreference(offset: Int): Unit = {
    val preferences = getSharedPreferences("READER_PREFERENCES", 0)
    val editor = preferences.edit()
    editor.putInt(ReaderPreferences.OFFSET, offset)
    editor.commit()
  }

  def endpointPreferenceValue: String =
    preferences.getString(
      ReaderPreferences.ENDPOINT,
      defaults.getProperty(ReaderProperties.DefaultEndpoint)
    )

  def offsetPreferenceValue: Int =
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

