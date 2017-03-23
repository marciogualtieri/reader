package com.wire.reader.helpers

import java.io.FileNotFoundException

import com.wire.reader.clients.http.HttpClient
import com.wire.reader.entitities.Message

import scala.util.{Failure, Success, Try}

abstract class MessageFetcher {
  def fetchedMessages(offset: Int): List[Message]
}

case class HttpMessageFetcher(endpoint: String) extends MessageFetcher {

  val client = new HttpClient
  val serializer = JsonMessageSerializer()

  override def fetchedMessages(offset: Int): List[Message] = {
    Try(client.get(s"$endpoint/$offset.json")) match {
      case Success(response) =>
        serializer.parseMessages(response, offset) ++ fetchedMessages(offset + 1)
      case Failure(_: FileNotFoundException) => List.empty[Message]
      case Failure(e: Throwable) => throw e
    }
  }
}