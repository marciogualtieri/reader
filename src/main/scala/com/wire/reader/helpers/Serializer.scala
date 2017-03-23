package com.wire.reader.helpers

import com.wire.reader.entitities.Message
import org.json4s.JsonAST.{JField, JInt, JObject, JString}
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{DefaultFormats, FieldSerializer, JValue}

trait Serializer {
  def parseFrom(response: String, index: Int): List[Message] = ???
}

trait JsonSerializer extends Serializer {

  override def parseFrom(response: String, index: Int): List[Message] = {
    implicit val formats = DefaultFormats + FieldSerializer[Message]()
    val json = parse(response)
    parseFrom(json, index)
  }

  private def parseFrom(json: JValue, index: Int): List[Message] = {
    for {
      JObject(items) <- json
      JField("id", JString(id)) <- items
      JField("time", JInt(time)) <- items
      JField("text", JString(text)) <- items
    } yield Message(id, time, text, index)
  }
}
