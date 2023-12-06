package com.uzabase.playtest2.http

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.Configuration.Companion.playtest2
import com.uzabase.playtest2.config.http
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.net.URI

class HttpStepsTest : FunSpec({
    val sut = HttpSteps()
    val server = WireMockServer(8080)

    beforeSpec {
        playtest2 {
            listOf(http(URI("http://localhost:8080").toURL()))
        }
        server.start()
    }

    beforeEach {
        // Simulate Gauge's ScenarioDataStore being cleared between scenarios
        ScenarioDataStore.items().forEach { k ->
            ScenarioDataStore.remove(k)
        }

        server.resetAll()
    }

    afterSpec() {
        server.stop()
    }

    test("Hello, world!") {
        sut.pathIntoRequest("/hello")
        sut.methodIntoRequest(HttpSteps.Method.GET)
        sut.sendRequest()
        val response = ScenarioDataStore.get(HttpSteps.K.RESPONSE) as okhttp3.Response
        response.body.string().shouldBe("Hello, world!")
    }

    context("Simple requests") {
        forAll(
            row(HttpSteps.Method.GET),
            row(HttpSteps.Method.DELETE),
            row(HttpSteps.Method.PUT),
            row(HttpSteps.Method.POST),
            row(HttpSteps.Method.PATCH)
        ) { method ->
            test("should be sent `$method` request correctly") {
                sut.pathIntoRequest("/articles")
                sut.methodIntoRequest(method)
                sut.sendRequest()

                server.verify(requestedFor(method.name, urlPathEqualTo("/articles")))
            }
        }
    }

    context("Request with headers") {
        forAll(
            row(emptyList()) { rpb: RequestPatternBuilder ->
                rpb
            },
            row(listOf("Accept: application/json")) { rpb: RequestPatternBuilder ->
                rpb.withHeader("Accept", equalTo("application/json"))
            },
            row(
                listOf(
                    "Content-Type: application/json; charset=us-ascii",
                    "Accept: application/json"
                )
            ) { rpb: RequestPatternBuilder ->
                rpb.withHeader("Content-Type", equalTo("application/json; charset=us-ascii"))
                    .withHeader("Accept", equalTo("application/json"))
            }
        ) { headers, f ->
            test("should be sent request with ${headers.size} headers correctly") {
                sut.pathIntoRequest("/articles")
                headers.forEach {
                    sut.headerIntoRequest(it)
                }
                sut.methodIntoRequest(HttpSteps.Method.GET)
                sut.sendRequest()

                server.verify(getRequestedFor(urlPathEqualTo("/articles")).let(f))
            }
        }
    }

    test("GET request with headers") {
        sut.pathIntoRequest("/articles")
        sut.methodIntoRequest(HttpSteps.Method.GET)
        sut.headerIntoRequest("Accept: application/json")
        sut.headerIntoRequest("Authorization: Bearer 1234567890")
        sut.sendRequest()

        server.verify(
            getRequestedFor(urlPathEqualTo("/articles"))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("Authorization", equalTo("Bearer 1234567890"))
        )
    }

    test("POST request with JSON body") {
        sut.pathIntoRequest("/articles")
        sut.jsonBodyIntoRequest("""{"title": "Hello, world!", "body": "This is a test."}""")
        sut.mediaTypeIntoRequest("application/json; charset=us-ascii")
        sut.methodIntoRequest(HttpSteps.Method.POST)
        sut.sendRequest()

        server.verify(
            postRequestedFor(urlPathEqualTo("/articles"))
                .withHeader("Content-Type", equalTo("application/json; charset=us-ascii"))
                .withRequestBody(equalToJson("""{"title": "Hello, world!", "body": "This is a test."}"""))
        )
    }

    context("Should throw an exception ") {
        forAll(
            row("Request Path") {
                sut.methodIntoRequest(HttpSteps.Method.POST)
            },
            row("Method") {
                sut.pathIntoRequest("/articles")
            },
            row("Media Type") {
                sut.pathIntoRequest("/articles")
                sut.methodIntoRequest(HttpSteps.Method.POST)
                sut.jsonBodyIntoRequest("""{}""")
            }
        ) { expected, prep ->
            test("if the `$expected` is not specified") {
                prep()
                val exception = shouldThrow<IllegalStateException> {
                    sut.sendRequest()
                }
                exception.message.shouldBe("You should specify the `$expected`")
            }
        }
    }
})
