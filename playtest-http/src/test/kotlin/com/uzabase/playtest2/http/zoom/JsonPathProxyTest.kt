package com.uzabase.playtest2.http.zoom

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class JsonPathProxyTest : FunSpec({
    context("Number") {
        context("ShouldBeBigDecimal") {
            test("should be equal") {
                JsonPathProxy.of("""{"price": 0.3}""", "$.price")
                    .shouldBe("0.3".toBigDecimal()).shouldBe(true)
            }
        }
    }
    context("Existence") {
        val json = """
                {
                    "name": "John",
                    "age": 30
                }
            """.trimIndent()

        context("shouldBeExist") {
            test("should be exist") {
                JsonPathProxy.of(json, "$.name")
                    .shouldBeExist().shouldBe(true)
            }

            test("should not be exist") {
                JsonPathProxy.of(json, "$.gender")
                    .shouldBeExist().shouldBe(false)
            }
        }

        context("shouldNotBeExist") {
            test("should be exist") {
                JsonPathProxy.of(json, "$.name")
                    .shouldNotBeExist().shouldBe(false)
            }

            test("should not be exist") {
                JsonPathProxy.of(json, "$.gender")
                    .shouldNotBeExist().shouldBe(true)
            }
        }

        test("complement") {
            forAll(
                row("$.name"),
                row("$.gender"),
                row("$.country"),
                row("$.age")
            ) { path ->
                val proxy = JsonPathProxy.of(json, path)
                proxy.shouldBeExist().shouldBe(!proxy.shouldNotBeExist())
            }
        }
    }

    context("complicated json-path evaluation") {
        val json = """
                {"people": [
                    {"name": "abc", "age": 21},
                    {"name": "def", "age": 28}
                ]}
            """.trimIndent()

        test("simple definite path") {
            JsonPathProxy.of(json, "$.people[0].name")
                .shouldBe("abc").shouldBeTrue()
        }

        test("indefinite path") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].age")
                .shouldBe(21).shouldBeTrue()
        }
    }
})
