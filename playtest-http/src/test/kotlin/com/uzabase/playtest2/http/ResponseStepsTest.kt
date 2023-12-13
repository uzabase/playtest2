package com.uzabase.playtest2.http

import com.github.tomakehurst.wiremock.WireMockServer
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.http.internal.K
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import okhttp3.OkHttpClient
import okhttp3.Request
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
        server.resetAll()
    }

    afterSpec {
        server.stop()
    }

    test("Hello, world") {
        client.newCall(Request.Builder().url(URI("http://localhost:8080/articles").toURL()).build())
                .execute().let { ScenarioDataStore.put(K.RESPONSE, it) }

        sut.assertStatusCode(200)
    }

    test("Failed test") {
        client.newCall(Request.Builder().url(URI("http://localhost:8080/articles").toURL()).build())
                .execute().let { ScenarioDataStore.put(K.RESPONSE, it) }

        val exception = shouldThrow<PlaytestException> {
            sut.assertStatusCode(400)
        }
        exception.message.shouldBe("Status code should be equal to 400")
    }
})