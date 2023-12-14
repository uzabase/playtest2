package com.uzabase.playtest2.http

import com.github.tomakehurst.wiremock.WireMockServer
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.http.internal.K
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.net.URI
import com.uzabase.playtest2.http.ResponseSteps as Sut

class ResponseStepsTest : FunSpec({
    val sut = Sut()
    val server = WireMockServer(8080)
    val client = OkHttpClient.Builder().build()

    beforeSpec {
        server.start()
    }

    beforeEach {
        ScenarioDataStore.items().forEach { ScenarioDataStore.remove(it) }
        server.resetAll()
    }

    afterSpec {
        server.stop()
    }

    context("About status code") {
        test("should be store as assertion target") {
            client.newCall(Request.Builder().url(URI("http://localhost:8080/articles").toURL()).build()).execute()
                .let { ScenarioDataStore.put(K.RESPONSE, it) }
            sut.statusCode()

            ScenarioDataStore.get(AssertionTarget).shouldBe(200L)
        }
    }

    context("About response body") {
        test("should be store as assertion target") {
            client.newCall(Request.Builder().url(URI("http://localhost:8080/articles").toURL()).build()).execute()
                .let { ScenarioDataStore.put(K.RESPONSE, it) }
            sut.body()

            ScenarioDataStore.get(AssertionTarget).shouldBeInstanceOf<ResponseBody>()
        }
    }

    context("About response headers") {
        test("should be store as assertion target") {
            client.newCall(Request.Builder().url(URI("http://localhost:8080/articles").toURL()).build()).execute()
                .let { ScenarioDataStore.put(K.RESPONSE, it) }
            sut.headers()

            ScenarioDataStore.get(AssertionTarget).shouldBeInstanceOf<Headers>()
        }
    }

    context("Assertions") {
        context("happy path") {
            test("long value") {
                ScenarioDataStore.put(AssertionTarget, 200L)
                sut.shouldBeLongValue(200L)
            }

            test("string value") {
                ScenarioDataStore.put(AssertionTarget, "Hello, world")
                sut.shouldBeStringValue("Hello, world")
            }
        }

        context("failed scenarios") {
            context("assertion target not found") {
                test("long value") {
                    shouldThrow<PlaytestException> {
                        sut.shouldBeLongValue(200L)
                    }.message.shouldBe("Assertion target is not found")
                }

                test("string value") {
                    shouldThrow<PlaytestException> {
                        sut.shouldBeStringValue("Hello, world")
                    }.message.shouldBe("Assertion target is not found")
                }
            }
        }
    }
})