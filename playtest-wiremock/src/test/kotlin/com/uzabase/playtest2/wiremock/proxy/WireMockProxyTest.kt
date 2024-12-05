package com.uzabase.playtest2.wiremock.proxy

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.uzabase.playtest2.core.assertion.Failed
import com.uzabase.playtest2.core.assertion.Ok
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

class WireMockProxyTest : FunSpec({

    val server = WireMockServer(8888)
    val client = HttpClient.newHttpClient()

    beforeSpec {
        server.start()
        server.stubFor(
            WireMock.get("/ping").willReturn(WireMock.aResponse().withBody("pong").withStatus(200))
        )
    }
    afterSpec {
        server.stop()
    }

    context("shouldBe") {
        beforeEach {
            WireMock(8888).resetRequests()
            client.send(
                HttpRequest.newBuilder().uri(URI("http://localhost:8888/ping")).build(),
                BodyHandlers.ofString()
            )
        }

        test("should be ok") {
            WireMockProxy.of(WireMock(8888), "GET", "/ping")
                .shouldBe(1)
                .shouldBe(Ok)
        }

        test("should be failed") {
            WireMockProxy.of(WireMock(8888), "GET", "/ping")
                .shouldBe(2)
                .shouldBeInstanceOf<Failed>()
        }
    }
})
