package com.wire.reader.helpers

import java.net.UnknownHostException

import com.wire.reader.entitities.Message
import com.wire.reader.test.helpers.TestHelpers
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class HttpFetcherTest extends FlatSpec with Matchers with BeforeAndAfterAll with TestHelpers {

  val fetcher = HttpFetcher("https://localhost:8443/reader")

  override def beforeAll: Unit = {
    WireMockServer.start()
    createEndpointStub("/reader/0.json", "application/json")
    createEndpointStub("/reader/1.json", "application/json")
    createEndpointStub("/reader/2.json", "application/json")
    createNotFoundStub("/reader/3.json")
  }

  override def afterAll: Unit = {
    WireMockServer.stop()
  }

  "Http ENDPOINT" should "return messages from first page" in {
    val messages = fetcher.messages(0)
    messages shouldBe TestMessagesEndpoint0 ++ TestMessagesEndpoint1 ++ TestMessagesEndpoint2
  }

  "Http ENDPOINT" should "return messages from second page." in {
    val messages = fetcher.messages(1)
    messages shouldBe  TestMessagesEndpoint1 ++ TestMessagesEndpoint2
  }

  "Http ENDPOINT" should "return messages for last page." in {
    val messages = fetcher.messages(2)
    messages shouldBe TestMessagesEndpoint2
  }

  "Http ENDPOINT" should "return no messages for non-existent page." in {
    val messages = fetcher.messages(3)
    messages shouldBe List.empty[Message]
  }

  "Http ENDPOINT" should "throws exception for non-existent host." in {
    intercept[UnknownHostException] {
      val fetcher = HttpFetcher("https://host.does.not.exist")
      fetcher.messages(0)
    }
  }

}
