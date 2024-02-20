package com.uzabase.playtest2.http.assertion

import com.uzabase.playtest2.http.JsonContainer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FromJsonTest : FunSpec({
    context("FromJson") {
        test("should convert value of JSON to String") {
            FromJson.create(JsonContainer("""
                {
                    "key": "value"
                }
            """.trimIndent(), "$.key")).asString().shouldBe("value")
        }
    }
})