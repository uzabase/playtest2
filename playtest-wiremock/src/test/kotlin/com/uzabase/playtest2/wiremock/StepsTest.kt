package com.uzabase.playtest2.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.uzabase.playtest2.core.AssertionSteps
import com.uzabase.playtest2.core.config.Configuration.Companion.playtest2
import com.uzabase.playtest2.wiremock.config.wireMock
import io.kotest.core.spec.style.FunSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URI

class StepsTest : FunSpec({
    val sut = Steps()
    val assert = AssertionSteps()
    val server = WireMockServer(3000).also { it.start() }

    val client = OkHttpClient().newBuilder().build()

    beforeSpec {
        playtest2 {
            listOf(wireMock("MyAPI", URI("http://localhost:3000").toURL()))
        }
        server.start()
    }

    beforeEach {
        server.resetAll()
    }

    afterSpec { server.stop() }

    context("Simple Request") {
        beforeEach {
            Request.Builder()
                .url("http://localhost:3000/hello")
                .build()
                .let { client.newCall(it).execute() }
                .close()
        }

        test("Assert GET /hello request should pass") {
            sut.setApi("MyAPI")
            sut.initParams("GET", "/hello")
            assert.shouldBeLongValue(1)
        }

        test("Assert GET /byebye reqeust should failed") {
            sut.setApi("MyAPI")
            sut.initParams("GET", "/byebye")
            assert.shouldBeLongValue(0)
        }
    }
})
