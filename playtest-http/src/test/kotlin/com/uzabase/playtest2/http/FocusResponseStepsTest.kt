package com.uzabase.playtest2.http

import com.github.tomakehurst.wiremock.WireMockServer
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.ShouldBeLong
import com.uzabase.playtest2.http.internal.K
import com.uzabase.playtest2.http.proxy.ResponseBodyProxy
import com.uzabase.playtest2.http.proxy.ResponseHeadersProxy
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.types.shouldBeInstanceOf
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import com.uzabase.playtest2.core.K as coreK
import com.uzabase.playtest2.http.FocusResponseSteps as Sut

class FocusResponseStepsTest : FunSpec({
    val sut = Sut()
    val server = WireMockServer(8080)
    val client = HttpClient.newHttpClient()

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
            client.send(HttpRequest.newBuilder(URI("http://localhost:8080/articles")).build(), BodyHandlers.ofString())
                .let { ScenarioDataStore.put(K.RESPONSE, it) }
            sut.statusCode()

            ScenarioDataStore.get(coreK.AssertionTarget)
                .let { it as ShouldBeLong }
                .shouldBe(200L).shouldBeTrue()
        }
    }

    context("About response body") {
        test("should be store as assertion target") {
            val f = Files.createTempFile("playtest2.test", "txt")
            f.toFile().deleteOnExit()
            client.send(HttpRequest.newBuilder(URI("http://localhost:8080/articles")).build(), BodyHandlers.ofFile(f))
                .let { ScenarioDataStore.put(K.RESPONSE, it) }
            sut.body()
            ScenarioDataStore.get(coreK.AssertionTarget).shouldBeInstanceOf<ResponseBodyProxy>()
        }
    }

    context("About response headers") {
        test("should be store as assertion target") {
            client.send(HttpRequest.newBuilder(URI("http://localhost:8080/articles")).build(), BodyHandlers.ofString())
                .let { ScenarioDataStore.put(K.RESPONSE, it) }
            sut.headers()

            ScenarioDataStore.get(coreK.AssertionTarget)
                .also { it.shouldBeInstanceOf<ResponseHeadersProxy>() }
        }
    }
})