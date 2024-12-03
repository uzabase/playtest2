package com.uzabase.playtest2.http.zoom

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.uzabase.playtest2.core.assertion.*
import java.math.BigDecimal

fun interface JsonSerializable {
    fun toJsonPathProxy(path: String): JsonPathProxy
}

sealed interface JsonPathProxy : ShouldBeString, ShouldContainsString, ShouldMatchString, ShouldBeLong,
    ShouldBeBigDecimal, ShouldBeBoolean, ShouldBeExist, ShouldNotBeExist, ShouldBeNull {
    companion object {
        fun of(json: String, path: String): JsonPathProxy =
            JsonPath.compile(path).let { compiled ->
                if (compiled.isDefinite) {
                    DefiniteJsonPathProxy(json, compiled)
                } else {
                    IndefiniteJsonPathProxy(json, compiled)
                }
            }
    }
}

internal class DefiniteJsonPathProxy(
    private val json: String,
    private val path: JsonPath
) : JsonPathProxy {

    private fun wrapPathNotFound(expected: Any, block: () -> TestResult): TestResult =
        try {
            block()
        } catch (e: PathNotFoundException) {
            Failed {
                """
                |Expected:
                |   value: $expected
                |   class: ${expected::class.qualifiedName}
                |Actual:
                |  error: ${e.message}
                |  json: $json
                """.trimMargin()
            }
        }

    override fun shouldBe(expected: String): Boolean = JsonPath.parse(json).read<String>(path) == expected
    override fun shouldContain(expected: String): Boolean = JsonPath.parse(json).read<String>(path).contains(expected)
    override fun shouldMatch(expected: String): Boolean =
        expected.toRegex().matches(JsonPath.parse(json).read<String>(path))

    override fun shouldBe(expected: Long): Boolean = JsonPath.parse(json).read<Int>(path).toLong() == expected
    override fun shouldBe(expected: BigDecimal): Boolean =
        JsonPath.parse(json).read<Double>(path).toBigDecimal() == expected

    override fun shouldBe(expected: Boolean): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<Boolean>(path).let {
                if (it == expected) {
                    Ok
                } else {
                    Failed {
                        """
                        |${simpleExplain(expected, it)}
                        |  json: $json
                        """.trimMargin()
                    }
                }
            }
        }

    override fun shouldBeExist(): TestResult =
        try {
            JsonPath.parse(json).read<Any>(path)
            Ok
        } catch (e: PathNotFoundException) {
            Failed {
                """
                |Expected:
                |  exists: true
                |Actual:
                |  exists: false
                |  json: $json
                """.trimMargin()
            }
        }

    override fun shouldNotBeExist(): TestResult =
        try {
            JsonPath.parse(json).read<Any>(path)
            Failed {
                """
                |Expected:
                |  exists: false
                |Actual:
                |  exists: true
                |  json: $json
                """.trimMargin()
            }
        } catch (e: PathNotFoundException) {
            Ok
        }

    override fun shouldBeNull(): Boolean =
        JsonPath.parse(json).read<Any?>(path) == null
}


internal class IndefiniteJsonPathProxy(
    private val json: String,
    private val path: JsonPath
) : JsonPathProxy {

    override fun shouldBe(expected: String): Boolean =
        JsonPath.parse(json).read<List<String>>(path).let { list ->
            if (list.size == 1) {
                list[0] == expected
            } else {
                throw PlaytestAssertionError("The path is indefinite and the result is not a single value")
            }
        }

    override fun shouldBe(expected: Long): Boolean =
        JsonPath.parse(json).read<List<Int>>(path).let { list ->
            if (list.size == 1) {
                list[0].toLong() == expected
            } else {
                throw PlaytestAssertionError("The path is indefinite and the result is not a single value")
            }
        }

    override fun shouldBe(expected: BigDecimal): Boolean =
        JsonPath.parse(json).read<List<Double>>(path).let { list ->
            if (list.size == 1) {
                list[0].toBigDecimal() == expected
            } else {
                throw PlaytestAssertionError("The path is indefinite and the result is not a single value")
            }
        }

    override fun shouldBe(expected: Boolean): TestResult =
        try {
            JsonPath.parse(json).read<List<Boolean>>(path).let { list ->
                if (list.size == 1 && list[0] == expected) {
                    Ok
                } else {
                    val expectedExplanation = """
                    |Expected:
                    |  value: $expected
                    |  class: ${expected::class.qualifiedName}
                    |
                """.trimMargin()

                    val actualExplanation = if (list.size == 1) {
                        """
                    |Actual:
                    |  value: ${list[0]}
                    |  class: ${list[0]::class.qualifiedName}
                    |
                """.trimMargin()
                    } else {
                        """
                    |Actual:
                    |  value: $list
                    |  error: The path is indefinite and the result is not a single value
                    |
                """.trimMargin()
                    }

                    Failed {
                        expectedExplanation + actualExplanation +
                                """|  json: $json""".trimMargin()
                    }
                }
            }
        } catch (e: PathNotFoundException) {
            Failed {
                """
                |Expected:
                |  value: $expected
                |  class: ${expected::class.qualifiedName}
                |Actual:
                |  error: ${e.message}
                |  json: $json
                """.trimMargin()
            }
        }

    override fun shouldContain(expected: String): Boolean =
        JsonPath.parse(json).read<List<String>>(path).let { list ->
            if (list.size == 1) {
                list[0].contains(expected)
            } else {
                throw PlaytestAssertionError("The path is indefinite and the result is not a single value")
            }
        }

    override fun shouldMatch(expected: String): Boolean =
        JsonPath.parse(json).read<List<String>>(path).let { list ->
            if (list.size == 1) {
                expected.toRegex().matches(list[0])
            } else {
                throw PlaytestAssertionError("The path is indefinite and the result is not a single value")
            }
        }

    /***
     * FIXME: Indefinite なパスの場合でも例外を返すケースがある( `filter` 操作の手前で存在しないパスを指すと Indefinite だけど例外)
     ***/
    override fun shouldBeExist(): TestResult =
        if (JsonPath.parse(json).read<List<Any>>(path).isNotEmpty()) {
            Ok
        } else {
            Failed {
                """
                |Expected:
                |  exists: true
                |Actual:
                |  exists: false
                |  json: $json
                """.trimMargin()
            }
        }

    override fun shouldNotBeExist(): TestResult =
        if (JsonPath.parse(json).read<List<Any>>(path).isEmpty()) {
            Ok
        } else {
            Failed {
                """
                |Expected:
                |  exists: false
                |Actual:
                |  exists: true
                |  json: $json
                """.trimMargin()
            }
        }

    override fun shouldBeNull(): Boolean =
        JsonPath.parse(json).read<List<Any?>>(path).let { list ->
            if (list.size == 1) {
                list[0] == null
            } else {
                throw PlaytestAssertionError("The path is indefinite and the result is not a single value")
            }
        }
}