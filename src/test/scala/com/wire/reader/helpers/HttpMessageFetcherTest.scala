package com.wire.reader.helpers

import java.net.UnknownHostException

import com.wire.reader.entitities.Message
import com.wire.reader.test.helpers.TestHelpers
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class HttpMessageFetcherTest extends FlatSpec with Matchers with BeforeAndAfterAll with TestHelpers {

  val fetcher = HttpMessageFetcher("https://localhost:8443/reader")

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

  "Http Fetcher" should "return messages from first page" in {
    val messages = fetcher.fetchedMessages(0)
    messages shouldBe TestMessagesEndpoint0 ++ TestMessagesEndpoint1 ++ TestMessagesEndpoint2
  }

  "Http Fetcher" should "return messages from second page." in {
    val messages = fetcher.fetchedMessages(1)
    messages shouldBe  TestMessagesEndpoint1 ++ TestMessagesEndpoint2
  }

  "Http Fetcher" should "return messages for last page." in {
    val messages = fetcher.fetchedMessages(2)
    messages shouldBe TestMessagesEndpoint2
  }

  "Http Fetcher" should "return no messages for non-existent page." in {
    val messages = fetcher.fetchedMessages(3)
    messages shouldBe List.empty[Message]
  }

  "Http Fetcher" should "throws exception for non-existent host." in {
    intercept[UnknownHostException] {
      val fetcher = HttpMessageFetcher("https://host.does.not.exist")
      fetcher.fetchedMessages(0)
    }
  }

}
