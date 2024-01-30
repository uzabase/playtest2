package com.uzabase.playtest2.http.assertion

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import okhttp3.Headers

class FromHeadersTest : FunSpec({
    context("FromHeaders") {
        forAll(
            row(
                Headers.headersOf(),
                ""
            ),
            row(
                Headers.headersOf("Content-Type", "application/json"),
                "Content-Type: application/json"
            ),
            row(
                Headers.headersOf(
                    "Content-Type", "application/json",
                    "Content-Length", "100"
                ),
                """
                    Content-Length: 100
                    Content-Type: application/json
                    """.trimIndent()
            )
        ) { headers, expected ->
            test("FromHeaders should convert Headers to String") {
                FromHeaders.create(headers).asString() shouldBe expected
            }
        }
    }
})