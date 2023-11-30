package com.uzabase.playtest2.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.uzabase.playtest2.Configuration.Companion.playtest2
import com.uzabase.playtest2.config.wireMock
import io.kotest.core.spec.style.FunSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URI

class StepsTest : FunSpec({
    val sut = Steps()
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
                .let { it.close() }
        }

        test("Assert GET /hello request should pass") {
            sut.setApiAndPath("MyAPI", "/hello")
            sut.assertRequestedAsGetRequest("MyAPI")
        }

        test("Assert GET /byebye reqeust should failed") {
            sut.setApiAndPath("MyAPI", "/byebye")
            sut.assertNotRequestedAsGetRequest("MyAPI")
        }
    }
})
