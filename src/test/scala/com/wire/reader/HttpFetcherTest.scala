package com.wire.reader

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import com.wire.test.TestHelper
import java.net.UnknownHostException

class HttpFetcherTest extends FlatSpec with Matchers with BeforeAndAfterAll with TestHelper {

  val endpoint = HttpFetcher("https://localhost:8443/reader")

  override def beforeAll: Unit = {
    wireMockServer.start()
    createEndpointStub("/reader/0.json", "application/json")
    createEndpointStub("/reader/1.json", "application/json")
    createEndpointStub("/reader/2.json", "application/json")
    createNotFoundStub("/reader/3.json")
  }

  override def afterAll: Unit = {
    wireMockServer.stop()
  }

  "Http Endpoint" should "return messages from first page" in {
    val messages = endpoint.messages(0)
    messages shouldBe TestMessagesEndpoint0 ++ TestMessagesEndpoint1 ++ TestMessagesEndpoint2
  }

  "Http Endpoint" should "return messages from second page." in {
    val messages = endpoint.messages(1)
    messages shouldBe  TestMessagesEndpoint1 ++ TestMessagesEndpoint2
  }

  "Http Endpoint" should "return messages for last page." in {
    val messages = endpoint.messages(2)
    messages shouldBe TestMessagesEndpoint2
  }

  "Http Endpoint" should "return no messages for non-existent page." in {
    val messages = endpoint.messages(3)
    messages shouldBe List.empty[Message]
  }

  "Http Endpoint" should "throws exception for non-existent host." in {
    intercept[UnknownHostException] {
      val endpoint = HttpFetcher("https://host.does.not.exist")
      val messages = endpoint.messages(0)
    }
  }

}
