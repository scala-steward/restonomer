package com.clairvoyant.restonomer.core.http

import com.clairvoyant.restonomer.core.exception.RestonomerException
import com.clairvoyant.restonomer.core.{CoreSpec, HttpMockSpec}
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sttp.client3._
import sttp.model.Method

class RestonomerRequestSpec extends CoreSpec with HttpMockSpec {

  "builder" should "return RestonomerRequestBuilder object" in {
    val method = Method.GET.method
    val url = "https://test-domain/url"

    RestonomerRequest.builder(method, url) shouldBe a[RestonomerRequestBuilder]
    RestonomerRequest.builder(method, url).httpRequest shouldBe a[Request[_, _]]
  }

  "send" should "return RestonomerResponse" in {
    val restonomerResponse = new RestonomerRequest(basicHttpRequest).send("HttpClientSyncBackend")

    restonomerResponse shouldBe a[RestonomerResponse]
    restonomerResponse.httpResponse shouldBe a[Identity[_]]
  }

  "send" should "return RestonomerResponse with the mocked response body" in {
    val responseBody = "Sample Response Body"

    stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(responseBody)))

    val restonomerResponse = new RestonomerRequest(basicHttpRequest).send("HttpClientSyncBackend")
    restonomerResponse.httpResponse.body.getOrElse() shouldBe responseBody
  }

  "send with invalid HttpBackendType" should "throw RestonomerException" in {
    the[RestonomerException] thrownBy new RestonomerRequest(basicHttpRequest).send(
      "ABCDBackendType"
    ) should have message "The http-backend-type: ABCDBackendType is not supported."
  }

}
