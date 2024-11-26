package com.uzabase.playtest2.http.zoom

import com.uzabase.playtest2.core.assertion.Failed
import com.uzabase.playtest2.core.assertion.Ok
import com.uzabase.playtest2.core.assertion.PlaytestAssertionError
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class JsonPathProxyTest : FunSpec({
    context("Boolean") {
        val json = """{"t": true, "f": false}"""
        test("user friendly failed message") {
            when (val r = JsonPathProxy.of(json, "$.f").shouldBe(true)) {
                is Failed -> r.explain().shouldBe(
                    """
                    Expected:
                      value: true
                      class: kotlin.Boolean
                    Actual:
                      value: false
                      class: kotlin.Boolean
                      json: {"t": true, "f": false}
                """.trimIndent()
                )

                else -> throw AssertionError("error")
            }
        }

        test("user should known does not exist expected value on path") {
            when (val r = JsonPathProxy.of(json, "$.i").shouldBe(false)) {
                is Failed -> r.explain().shouldBe(
                    """
                |Expected:
                |  value: false
                |  class: kotlin.Boolean
                |Actual:
                |  error: No results for path: $['i']
                |  json: {"t": true, "f": false}
                """.trimMargin()
                )

                else -> throw AssertionError("error")
            }
        }
    }
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

    context("indefinite json path") {
        val json = """
                {"people": [
                    {"name": "abc", "age": 21, "pocketMoney": 1.2, "worker?": true},
                    {"name": "def", "age": 28, "pocketMoney": 2.3, "worker?": false}
                ]}
            """.trimIndent()

        test("should be true if single string value") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].name")
                .shouldBe("abc").shouldBeTrue()
        }

        test("should be failed if multiple string values") {
            shouldThrow<PlaytestAssertionError> {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].name")
                    .shouldBe("abc")
            }.message.shouldBe("The path is indefinite and the result is not a single value")
        }

        test("should be true if single long value") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].age")
                .shouldBe(21).shouldBeTrue()
        }

        test("should be failed if multiple long values") {
            shouldThrow<PlaytestAssertionError> {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].age")
                    .shouldBe(999)
            }.message.shouldBe("The path is indefinite and the result is not a single value")
        }

        test("should be true if single double value") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].pocketMoney")
                .shouldBe(1.2.toBigDecimal()).shouldBeTrue()
        }

        test("should be failed if multiple double values") {
            shouldThrow<PlaytestAssertionError> {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].pocketMoney")
                    .shouldBe(9.9.toBigDecimal())
            }.message.shouldBe("The path is indefinite and the result is not a single value")
        }

        test("should return Ok if single boolean value is corrected") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].worker?")
                .shouldBe(true).shouldBe(Ok)
        }

        test("should return Failed if single boolean value is not corrected") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].worker?")
                .shouldBe(false).let {
                    when (it) {
                        is Failed -> it.explain().shouldBe(
                            """
                            |Expected:
                            |  value: false
                            |  class: kotlin.Boolean
                            |Actual:
                            |  value: true
                            |  class: kotlin.Boolean
                            |  json: $json
                        """.trimMargin()
                        )

                        else -> throw AssertionError("error")
                    }
                }
        }

        test("should return Failed if multiple boolean values") {
            JsonPathProxy.of(json, "$.people[?(@.age > 1)].worker?")
                .shouldBe(false)
                .let {
                    when (it) {
                        is Failed -> it.explain().shouldBe(
                            """
                            |Expected:
                            |  value: false
                            |  class: kotlin.Boolean
                            |Actual:
                            |  value: [true,false]
                            |  error: The path is indefinite and the result is not a single value
                            |  json: $json
                        """.trimMargin()
                        )

                        else -> throw AssertionError("error")
                    }
                }
        }

        test("should return Failed if path is not exists") {
            JsonPathProxy.of(json, "$.__does_not_exists__.[?(@ > 42)].answer")
                .shouldBe(false)
                .let {
                    when (it) {
                        is Failed -> it.explain().shouldBe(
                            """
                            |Expected:
                            |  value: false
                            |  class: kotlin.Boolean
                            |Actual:
                            |  error: Missing property in path $['__does_not_exists__']
                            |  json: $json
                        """.trimMargin()
                        )

                        else -> throw AssertionError("error")
                    }
                }
        }

        test("should be true if single string value - contains") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].name")
                .shouldContain("b").shouldBeTrue()
        }

        test("should be failed if multiple string values - contains") {
            shouldThrow<PlaytestAssertionError> {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].name")
                    .shouldContain("b")
            }.message.shouldBe("The path is indefinite and the result is not a single value")
        }

        test("should be true if single string value - entire match") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].name")
                .shouldMatch("[abc]{3}").shouldBeTrue()
        }

        test("should be failed if multiple string values - entire match") {
            shouldThrow<PlaytestAssertionError> {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].name")
                    .shouldMatch(".*")
            }.message.shouldBe("The path is indefinite and the result is not a single value")
        }

        test("should be false if empty after filtering - should be exist") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'xyz')].name")
                .shouldBeExist().shouldBe(false)
        }

        test("should be true if not empty after filtering - should be exist") {
            JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].name")
                .shouldBeExist().shouldBe(true)
        }

        test("should be false if path not found - should be exist") {
            JsonPathProxy.of(json, "$.people[?(@.age > 0)][999]")
                .shouldBeExist().shouldBeFalse()
        }
    }

    context("Null") {
        val json = """
            {"nullValue": null,
             "nonNullValue": 42, 
             "xs": [null, 1, 2, 3],
             "objects": [{"x": null},
                         {},
                         {"x": 1}]}
        """.trimIndent()

        test("should be true if definite path is null") {
            forAll(
                row("$.nullValue"),
                row("$.xs[0]"),
                row("$.objects[0].x")
            ) { path ->
                JsonPathProxy.of(json, path).shouldBeNull().shouldBeTrue()
            }
        }

        test("should be false if definite path is not null") {
            forAll(
                row("$.nonNullValue"),
                row("$.xs[1]"),
                row("$.objects[2].x")
            ) { path ->
                JsonPathProxy.of(json, path).shouldBeNull().shouldBeFalse()
            }
        }

        test("should be true if indefinite path is null") {
            forAll(
                row("$.xs[?(@ == null)]"),
                row("$.objects[?(@.x == null)].x")
            ) { path ->
                JsonPathProxy.of(json, path).shouldBeNull().shouldBeTrue()
            }
        }

        test("should be false if indefinite path is not null") {
            forAll(
                row("$.xs[?(@ == 1)]"),
                row("$.objects[?(@.x == 1)].x")
            ) { path ->
                JsonPathProxy.of(json, path).shouldBeNull().shouldBeFalse()
            }
        }
    }
})
