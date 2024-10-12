package com.uzabase.playtest2.http.proxy

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import java.net.http.HttpHeaders

class FromHeadersTest : FunSpec({
    context("FromHeaders") {
        forAll(
            row(
                HttpHeaders.of(mapOf(), { _, _ -> true }),
                ""
            ),
            row(
                HttpHeaders.of(mapOf("Content-Type" to listOf("application/json")), { _, _ -> true }),
                "Content-Type: application/json"
            ),
            row(
                HttpHeaders.of(
                    mapOf(
                        "Content-Type" to listOf("application/json"),
                        "Content-Length" to listOf("100")
                    ),
                    { _, _ -> true }
                ),
                """
                    Content-Length: 100
                    Content-Type: application/json
                    """.trimIndent()
            ),
        ) { headers, expected ->
            test("FromHeaders should convert Headers to String -- for $headers") {
                headers.let(ResponseHeadersProxy::of).shouldBe(expected)
            }
        }
    }
})