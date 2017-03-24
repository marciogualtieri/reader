package com.wire.reader.test.helpers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, get, stubFor, urlEqualTo}
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.wire.reader.entitities.Message

import scala.io.Source

trait TestHelper {

  //scalastyle:off
  val TestMessagesEndpoint0 = List(
    Message(id = "george1", time = 1463372338305L, text = "You’ve got to apologize.", index = 0),
    Message(id = "jerry1", time = 1463372338485L, text = "Why?", index = 0),
    Message(id = "george2", time = 1463372338665L, text = "Because it’s the mature and adult thing to do.", index = 0),
    Message(id = "jerry2", time = 1463372338845L, text = "How does that affect me?", index = 0)
  )

  val TestMessagesEndpoint1 = List(
    Message(id = "customer1", time = 1463372339025L, text = "Now, what is the difference between the GT and the GTS?", index = 1),
    Message(id = "larry1", time = 1463372339205L, text = "OK, the GTS is \"guaranteed tremendous safety\".", index = 1),
    Message(id = "customer2", time = 1463372339385L, text = "So, without the \"S\", it's just \"guaranteed tremendous\"?", index = 1)
  )

  val TestMessagesEndpoint2 = List(
    Message(id = "robot1", time = 1463372339565L, text = "What's my purpose?", index = 2),
    Message(id = "rick1", time = 1463372339745L, text = "You pass butter.", index = 2),
    Message(id = "robot2", time = 1463372339925L, text = "Oh, my god...", index = 2),
    Message(id = "rick2", time = 1463372340105L, text = "Yeah. Welcome to the club, pal.", index = 2),
    Message(id = "rick3", time = 1463372340285L, text = "https://rawgit.com/marciogualtieri/reader/master/src/test/resources/reader/rickandmorty.png", index = 2),
    Message(id = "rick4", time = 1463372340465L, text = "https://this.is.a/broken/link/image/morty.png", index = 2)
  )
  //scalastyle:on

  val AllTestMessages: List[Message] = TestMessagesEndpoint0 ++ TestMessagesEndpoint1 ++ TestMessagesEndpoint2

  private val KeyPath: String = getClass.getResource("/server.jks").getPath

  val WireMockServer = new WireMockServer(wireMockConfig()
    .httpsPort(8443)
    .needClientAuth(false)
    .trustStorePath(KeyPath)
    .trustStorePassword("password"))

  def createEndpointStub(endpoint: String, contentType: String): Unit = {
    val content = Source.fromURL(getClass.getResource(endpoint)).mkString
    stubFor(get(urlEqualTo(endpoint))
      .willReturn(
        aResponse()
          .withBody(content)
          .withHeader("Content-Type", contentType)
          .withStatus(200)))
  }

  def createNotFoundStub(endpoint: String): Unit =
    stubFor(get(urlEqualTo(endpoint))
    .willReturn(
      aResponse()
        .withStatus(404)))
}