package com.wire.reader.helpers

import java.io.FileNotFoundException

import com.wire.reader.clients.http.HttpClient
import com.wire.reader.entitities.Message

import scala.util.{Failure, Success, Try}

abstract class Fetcher {
  def messages(offset: Int): List[Message]
}

case class HttpFetcher(endpoint: String) extends Fetcher with JsonSerializer {

  val client = new HttpClient

  override def messages(offset: Int): List[Message] = {
    Try(client.get(s"$endpoint/$offset.json")) match {
      case Success(response) =>
       parseFrom(response, offset) ++ messages(offset + 1)
      case Failure(_: FileNotFoundException) => List.empty[Message]
      case Failure(e: Throwable) => throw e
    }
  }
}