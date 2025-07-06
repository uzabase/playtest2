package com.uzabase.playtest2.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.uzabase.playtest2.core.AssertionSteps
import com.uzabase.playtest2.core.config.Configuration.Companion.playtest2
import com.uzabase.playtest2.wiremock.config.wireMock
import io.kotest.core.spec.style.FunSpec
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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
        WireMock(3000).resetRequests()
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
            sut.setupApi("MyAPI", "GET", "/hello")
            assert.shouldBeLongValue(1)
        }

        test("Assert GET /byebye reqeust should failed") {
            sut.setupApi("MyAPI", "GET", "/byebye")
            assert.shouldBeLongValue(0)
        }
    }


    test("Complicated GET Request") {
        Request.Builder().url("http://localhost:3000/path?p1=a&p1=b&p2=42")
            .header("great-answer", "42")
            .build()
            .let { client.newCall(it).execute() }
            .close()

        sut.setupApi("MyAPI", "GET", "/path")
        sut.query("p1", "a")
        sut.query("p1", "b")
        sut.query("p2", "42")
        sut.header("great-answer", "42")
        assert.shouldBeLongValue(1)
    }

    test("Complicated POST Request") {
        Request.Builder()
            .url("http://localhost:3000/path")
            .header("great-answer", "42")
            .post("{\"a\":\"Hello\", \"b\": 42}".toRequestBody("application/json".toMediaType()))
            .build()
            .let { client.newCall(it).execute() }
            .close()

        sut.setupApi("MyAPI", "POST", "/path")
        sut.jsonBody("{\"b\": 42, \"a\": \"Hello\"}")
        sut.header("great-answer", "42")
        assert.shouldBeLongValue(1)
    }

    context("Regex Path Request") {
        beforeEach {
            Request.Builder()
                .url("http://localhost:3000/sessions/6680a85e-0919-4472-a65f-020ed96d9717")
                .post("".toRequestBody("application/json".toMediaType()))
                .build()
                .let { client.newCall(it).execute() }
                .close()
        }

        test("Assert POST with regex path should pass") {
            sut.setupApiWithRegexPath("MyAPI", "POST", "/sessions/.+")
            assert.shouldBeLongValue(1)
        }

        test("Assert POST with different regex pattern should fail") {
            sut.setupApiWithRegexPath("MyAPI", "POST", "/_/sessions/.+")
            assert.shouldBeLongValue(0)
        }

        test("Assert GET with same regex path should fail for different method") {
            sut.setupApiWithRegexPath("MyAPI", "GET", "/sessions/.+")
            assert.shouldBeLongValue(0)
        }
    }
})
