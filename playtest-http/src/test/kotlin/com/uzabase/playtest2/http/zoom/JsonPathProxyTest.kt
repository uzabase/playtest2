package com.uzabase.playtest2.http.zoom

import com.uzabase.playtest2.core.assertion.Failed
import com.uzabase.playtest2.core.assertion.Ok
import com.uzabase.playtest2.core.assertion.PlaytestAssertionError
import com.uzabase.playtest2.core.assertion.TestResult
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class JsonPathProxyTest : FunSpec({
    context("Boolean") {
        val json = """{"t": true, "f": false}"""
        test("should failed when expected value is not equal") {
            JsonPathProxy.of(json, "$.f").shouldBe(true).shouldBeInstanceOf<Failed>()
        }

        test("should failed when jsonpath value is not exists") {
            JsonPathProxy.of(json, "$.i").shouldBe(false).shouldBeInstanceOf<Failed>()
        }
    }
    context("Number") {
        context("ShouldBeBigDecimal") {
            test("should be equal") {
                JsonPathProxy.of("""{"price": 0.3}""", "$.price")
                    .shouldBe("0.3".toBigDecimal()).shouldBe(Ok)
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
                    .shouldBeExist().shouldBe(Ok)
            }

            test("should not be exist") {
                JsonPathProxy.of(json, "$.gender")
                    .shouldBeExist()
                    .shouldBeInstanceOf<Failed>()
            }
        }

        context("shouldNotBeExist") {
            test("should be exist") {
                JsonPathProxy.of(json, "$.name")
                    .shouldNotBeExist().shouldBeInstanceOf<Failed>()
            }

            test("should not be exist") {
                JsonPathProxy.of(json, "$.gender")
                    .shouldNotBeExist().shouldBe(Ok)
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
                (proxy.shouldBeExist() is Ok).shouldBe(proxy.shouldNotBeExist() is Failed)
            }
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
                JsonPathProxy.of(json, path).shouldBeNull().shouldBe(Ok)
            }
        }

        test("should be false if definite path is not null") {
            forAll(
                row("$.nonNullValue"),
                row("$.xs[1]"),
                row("$.objects[2].x")
            ) { path ->
                JsonPathProxy.of(json, path).shouldBeNull().shouldBeInstanceOf<Failed>()
            }
        }

        test("should be true if indefinite path is null") {
            forAll(
                row("$.xs[?(@ == null)]"),
                row("$.objects[?(@.x == null)].x")
            ) { path ->
                JsonPathProxy.of(json, path).shouldBeNull().shouldBe(Ok)
            }
        }

        test("should be false if indefinite path is not null") {
            forAll(
                row("$.xs[?(@ == 1)]"),
                row("$.objects[?(@.x == 1)].x")
            ) { path ->
                JsonPathProxy.of(json, path).shouldBeNull().shouldBeInstanceOf<Failed>()
            }
        }
    }

    context("Definite Path") {
        val json = """
            |{"stringValue": "abc",
            | "trueValue": true,
            | "nullValue": null,
            | "intValue": 42}
        """.trimMargin()

        context("Failed messages are") {
            forAll(
                row(
                    "$.trueValue", { pxy: JsonPathProxy -> pxy.shouldBe(false) },
                    """
                    |Expected:
                    |  value: false
                    |  class: kotlin.Boolean
                    |Actual:
                    |  value: true
                    |  class: kotlin.Boolean
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.__missing__", { pxy: JsonPathProxy -> pxy.shouldBeExist() },
                    """
                    |Expected:
                    |  exists: true
                    |Actual:
                    |  exists: false
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.trueValue", { pxy: JsonPathProxy -> pxy.shouldNotBeExist() },
                    """
                    |Expected:
                    |  exists: false
                    |Actual:
                    |  exists: true
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.trueValue", { pxy: JsonPathProxy -> pxy.shouldBeNull() },
                    """
                    |Expected:
                    |  value: null
                    |  class: null
                    |Actual:
                    |  value: true
                    |  class: kotlin.Boolean
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.intValue", { pxy: JsonPathProxy -> pxy.shouldBe(43) },
                    """
                    |Expected:
                    |  value: 43
                    |  class: kotlin.Long
                    |Actual:
                    |  value: 42
                    |  class: kotlin.Long
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.nullValue", { pxy: JsonPathProxy -> pxy.shouldBe(43) },
                    """
                    |Expected:
                    |  value: 43
                    |  class: kotlin.Long
                    |Actual:
                    |  value: null
                    |  class: null
                    |  json: $json
                    """.trimMargin()
                )
            ) { path, expr, expected ->
                test("$path for $json") {
                    JsonPathProxy.of(json, path).let(expr).let { result: TestResult ->
                        when (result) {
                            is Failed -> result.explain().shouldBe(expected)
                            else -> throw AssertionError("error")
                        }
                    }
                }
            }
        }
    }

    context("Indefinite Path") {
        val json = """
                {"people": [
                    {"name": "abc", "age": 21, "pocketMoney": 1.2, "worker?": true},
                    {"name": "def", "age": 28, "pocketMoney": 2.3, "worker?": false}
                ]}
            """.trimIndent()

        context("ok or failed") {
            test("should be true if single string value") {
                JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].name")
                    .shouldBe("abc").shouldBe(Ok)
            }

            test("should be failed if multiple string values") {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].name")
                    .shouldBe("abc")
                    .shouldBeInstanceOf<Failed>()
            }

            test("should be true if single long value") {
                JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].age")
                    .shouldBe(21).shouldBe(Ok)
            }

            test("should be failed if multiple long values") {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].age")
                    .shouldBe(999)
                    .shouldBeInstanceOf<Failed>()
            }

            test("should be true if single double value") {
                JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].pocketMoney")
                    .shouldBe(1.2.toBigDecimal()).shouldBe(Ok)
            }

            test("should be failed if multiple double values") {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].pocketMoney")
                    .shouldBe(9.9.toBigDecimal()).shouldBeInstanceOf<Failed>()
            }

            test("should return Ok if single boolean value is corrected") {
                JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].worker?")
                    .shouldBe(true).shouldBe(Ok)
            }

            test("should return Failed if single boolean value is not corrected") {
                JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].worker?")
                    .shouldBe(false)
                    .shouldBeInstanceOf<Failed>()
            }

            test("should return Failed if multiple boolean values") {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].worker?")
                    .shouldBe(false)
                    .shouldBeInstanceOf<Failed>()
            }

            test("should return Failed if path is not exists") {
                JsonPathProxy.of(json, "$.__does_not_exists__.[?(@ > 42)].answer")
                    .shouldBe(false)
                    .shouldBeInstanceOf<Failed>()
            }

            test("should be true if single string value - contains") {
                JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].name")
                    .shouldContain("b").shouldBe(Ok)
            }

            test("should be failed if multiple string values - contains") {
                JsonPathProxy.of(json, "$.people[?(@.age > 1)].name")
                    .shouldContain("b").shouldBeInstanceOf<Failed>()
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

            test("should return Failed if empty after filtering - should be exist") {
                JsonPathProxy.of(json, "$.people[?(@.name == 'xyz')].name")
                    .shouldBeExist()
                    .shouldBeInstanceOf<Failed>()
            }

            test("should be true if not empty after filtering - should be exist") {
                JsonPathProxy.of(json, "$.people[?(@.name == 'abc')].name")
                    .shouldBeExist().shouldBe(Ok)
            }

            test("should return Failed if path not found - should be exist") {
                JsonPathProxy.of(json, "$.people[?(@.age > 0)][999]")
                    .shouldBeExist()
                    .shouldBeInstanceOf<Failed>()
            }
        }

        context("failed messages are") {
            forAll(
                row(
                    "$.__does_not_exists__.[?(@ > 42)].answer", { pxy: JsonPathProxy -> pxy.shouldBe(false) },
                    """
                    |Expected:
                    |  value: false
                    |  class: kotlin.Boolean
                    |Actual:
                    |  error: Missing property in path $['__does_not_exists__']
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.people[?(@.name == 'abc')].name", { pxy: JsonPathProxy -> pxy.shouldBe("abcd") },
                    """
                    |Expected:
                    |  value: abcd
                    |  class: kotlin.String
                    |Actual:
                    |  value: abc
                    |  class: kotlin.String
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.people[?(@.age > 1)].name", { pxy: JsonPathProxy -> pxy.shouldBe("abc") },
                    """
                    |Expected:
                    |  value: abc
                    |  class: kotlin.String
                    |Actual:
                    |  value: ["abc","def"]
                    |  error: The path is indefinite and the result is not a single value
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.people[?(@.age > 1)].worker?", { pxy: JsonPathProxy -> pxy.shouldBe(false) },
                    """
                    |Expected:
                    |  value: false
                    |  class: kotlin.Boolean
                    |Actual:
                    |  value: [true,false]
                    |  error: The path is indefinite and the result is not a single value
                    |  json: $json
                    """.trimMargin()
                ),
                row(
                    "$.people[?(@.name == 'abc')].worker?", { pxy: JsonPathProxy -> pxy.shouldBe(false) },
                    """
                    |Expected:
                    |  value: false
                    |  class: kotlin.Boolean
                    |Actual:
                    |  value: true
                    |  class: kotlin.Boolean
                    |  json: {"people": [
                    |    {"name": "abc", "age": 21, "pocketMoney": 1.2, "worker?": true},
                    |    {"name": "def", "age": 28, "pocketMoney": 2.3, "worker?": false}
                    |]}
                    """.trimMargin()
                ),
                row(
                    "$.people[?(@.name == 'abc')].worker?", { pxy: JsonPathProxy -> pxy.shouldBeNull() },
                    """
                    |Expected:
                    |  value: null
                    |  class: null
                    |Actual:
                    |  value: true
                    |  class: kotlin.Boolean
                    |  json: {"people": [
                    |    {"name": "abc", "age": 21, "pocketMoney": 1.2, "worker?": true},
                    |    {"name": "def", "age": 28, "pocketMoney": 2.3, "worker?": false}
                    |]}
                    """.trimMargin()
                )
            ) { jsonPath, f, expected ->
                test("$jsonPath for $json") {
                    JsonPathProxy.of(json, jsonPath).let(f).let {
                        when (it) {
                            is Failed -> it.explain().shouldBe(expected)

                            else -> throw AssertionError("error")
                        }
                    }
                }
            }
        }
    }
})
