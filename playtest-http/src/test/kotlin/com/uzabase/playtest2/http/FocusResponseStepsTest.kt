package com.uzabase.playtest2.http

import com.github.tomakehurst.wiremock.WireMockServer
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.Assertable
import com.uzabase.playtest2.http.internal.K
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.net.URI
import com.uzabase.playtest2.core.K as coreK
import com.uzabase.playtest2.http.FocusResponseSteps as Sut

class FocusResponseStepsTest : FunSpec({
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

            ScenarioDataStore.get(coreK.AssertionTarget)
                .let { it as Assertable }
                .asLong().shouldBe(200L)
        }
    }

    context("About response body") {
        test("should be store as assertion target") {
            client.newCall(Request.Builder().url(URI("http://localhost:8080/articles").toURL()).build()).execute()
                .let { ScenarioDataStore.put(K.RESPONSE, it) }
            sut.body()

            ScenarioDataStore.get(coreK.AssertionTarget).shouldBeInstanceOf<ResponseBody>()
        }
    }

    context("About response headers") {
        test("should be store as assertion target") {
            client.newCall(Request.Builder().url(URI("http://localhost:8080/articles").toURL()).build()).execute()
                .let { ScenarioDataStore.put(K.RESPONSE, it) }
            sut.headers()

            ScenarioDataStore.get(coreK.AssertionTarget).shouldBeInstanceOf<Headers>()
        }
    }
})