package com.uzabase.playtest2.json.proxy

import com.uzabase.playtest2.json.JsonPathContext
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FromJsonTest : FunSpec({
    context("FromJson") {
        test("should calculate JSONPath context into String value") {
            FromJson.create(JsonPathContext("""
                {
                    "key": "value"
                }
            """.trimIndent(), "$.key")).asString()
                .shouldBe("value")
        }

        test("should calculate JSONPath context into Long value") {
            FromJson.create(JsonPathContext("""
                {
                    "key": 42
                }
            """.trimIndent(), "$.key")).asLong()
                .shouldBe(42L)
        }
    }
})