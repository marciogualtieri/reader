package com.wire.reader

import java.io.FileNotFoundException

import com.wire.reader.clients.http.HttpClient
import org.json4s.JsonAST.{JField, JInt, JObject, JString}
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{DefaultFormats, FieldSerializer, JValue}

case class Message(id: String, time: BigInt, text: String, index: Long)

import scala.util.{Failure, Success, Try}

abstract class Fetcher {
  def messages(offset: Int): List[Message]
}

case class HttpFetcher(endpoint: String) extends Fetcher {

  val client = new HttpClient

  override def messages(offset: Int): List[Message] = {
    Try(client.get(s"$endpoint/$offset.json")) match {
      case Success(response) =>
       messagesFromResponse(response, offset) ++ messages(offset + 1)
      case Failure(_: FileNotFoundException) => List.empty[Message]
      case Failure(e: Throwable) => throw e
    }
  }

  private def messagesFromResponse(response: String, index: Int): List[Message] = {
    implicit val formats = DefaultFormats + FieldSerializer[Message]()
    val json = parse(response)
    messagesFromJson(json, index)
  }

  private def messagesFromJson(json: JValue, index: Int): List[Message] = {
    for {
      JObject(items) <- json
      JField("id", JString(id)) <- items
      JField("time", JInt(time)) <- items
      JField("text", JString(text)) <- items
    } yield Message(id, time, text, index)
  }
}