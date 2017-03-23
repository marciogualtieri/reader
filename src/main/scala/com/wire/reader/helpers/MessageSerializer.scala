package com.wire.reader.helpers

import com.wire.reader.entitities.Message
import org.json4s.JsonAST._
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization.write
import org.json4s.{DefaultFormats, FieldSerializer, Formats, JValue}

abstract class MessageSerializer {
  def parseMessages(response: String, index: Int): List[Message]
  def parseMessages(response: String): List[Message]
  def composeMessages(messages: List[Message]): String
}

case class JsonMessageSerializer() extends MessageSerializer {

  implicit val formats: Formats =
    DefaultFormats + FieldSerializer[Message]()

  override def parseMessages(response: String, index: Int): List[Message] = {
    val json = parse(response)
    parseMessagesFromJson(json, index)
  }

  override def parseMessages(response: String): List[Message] = {
    val json = parse(response)
    parseMessagesFromJson(json)
  }

  override def composeMessages(messages: List[Message]): String =
    write(messages)

  private def parseMessagesFromJson(json: JValue, index: Int): List[Message] = {
    for {
      JObject(items) <- json
      JField("id", JString(id)) <- items
      JField("time", JInt(time)) <- items
      JField("text", JString(text)) <- items
    } yield Message(id, time, text, index)
  }

  private def parseMessagesFromJson(json: JValue): List[Message] = {
    for {
      JObject(items) <- json
      JField("id", JString(id)) <- items
      JField("time", JInt(time)) <- items
      JField("text", JString(text)) <- items
      JField("index", JInt(index)) <- items
    } yield Message(id, time, text, index.toInt)
  }
}
