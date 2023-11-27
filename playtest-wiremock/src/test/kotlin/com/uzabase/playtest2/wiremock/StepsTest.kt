package com.uzabase.playtest2.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import io.kotest.core.spec.style.FunSpec
import okhttp3.OkHttpClient
import okhttp3.Request

class StepsTest : FunSpec({
    val sut = Steps()
    val server = WireMockServer(8080).also { it.start() }

    val client = OkHttpClient().newBuilder().build()

    beforeContainer { server.start() }

    beforeEach {
        server.resetAll()
    }

    afterContainer { server.stop() }

    context("Simple Request") {
        beforeEach {
            Request.Builder()
                .url("http://localhost:8080/hello")
                .build().let { client.newCall(it).execute() }
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
