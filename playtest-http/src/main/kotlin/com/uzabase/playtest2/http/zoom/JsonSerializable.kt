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

    private fun wrapPathNotFound(expected: Any?, block: () -> TestResult): TestResult =
        try {
            block()
        } catch (e: PathNotFoundException) {
            Failed {
                """
                |Expected:
                |   value: $expected
                |   class: ${
                    if (expected == null) {
                        "null"
                    } else {
                        expected::class.qualifiedName
                    }
                }
                |Actual:
                |  error: ${e.message}
                |  json: $json
                """.trimMargin()
            }
        }

    override fun shouldBe(expected: String): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<String>(path).let {
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

    override fun shouldContain(expected: String): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<String>(path).let {
                if (it.contains(expected)) {
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

    override fun shouldMatch(expected: String): Boolean =
        expected.toRegex().matches(JsonPath.parse(json).read<String>(path))

    override fun shouldBe(expected: Long): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<Int>(path).let {
                if (it?.toLong() == expected) {
                    Ok
                } else {
                    Failed {
                        """
                        |${simpleExplain(expected, it?.toLong())}
                        |  json: $json
                        """.trimMargin()
                    }
                }
            }
        }

    override fun shouldBe(expected: BigDecimal): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<Double>(path).let {
                if (it?.toBigDecimal() == expected) {
                    Ok
                } else {
                    Failed {
                        """
                        |${simpleExplain(expected, it?.toBigDecimal())}
                        |  json: $json
                        """.trimMargin()
                    }
                }
            }

        }

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

    override fun shouldBeNull(): TestResult =
        wrapPathNotFound(null) {
            JsonPath.parse(json).read<Any?>(path).let {
                if (it == null) {
                    Ok
                } else {
                    Failed {
                        """
                        |${simpleExplain(null, it)}
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
}


internal class IndefiniteJsonPathProxy(
    private val json: String,
    private val path: JsonPath
) : JsonPathProxy {

    private fun wrapPathNotFound(expected: Any?, block: () -> TestResult): TestResult =
        try {
            block()
        } catch (e: PathNotFoundException) {
            Failed {
                """
                |Expected:
                |  value: $expected
                |  class: ${
                    if (expected == null) {
                        "null"
                    } else {
                        expected::class.qualifiedName
                    }
                }
                |Actual:
                |  error: ${e.message}
                |  json: $json
                """.trimMargin()
            }
        }

    private fun listExplain(
        expected: Any?,
        list: List<Any?>
    ): String {
        val expectedExplanation =
            """
            |Expected:
            |  value: $expected
            |  class: ${
                if (expected == null) {
                    "null"
                } else {
                    expected::class.qualifiedName
                }
            }
            """.trimMargin()

        val actualExplanation = if (list.size == 1) {
            """
            |Actual:
            |  value: ${list[0]}
            |  class: ${
                if (list[0] == null) {
                    "null"
                } else {
                    list[0]!!::class.qualifiedName
                }
            }
            """.trimMargin()
        } else {
            """
            |Actual:
            |  value: $list
            |  error: The path is indefinite and the result is not a single value
            """.trimMargin()
        }

        return """
               |$expectedExplanation
               |$actualExplanation
               |  json: $json
               """.trimMargin()

    }

    override fun shouldBe(expected: String): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<List<String>>(path).let { list ->
                if (list.size == 1 && list[0] == expected) {
                    Ok
                } else {
                    Failed { listExplain(expected, list) }
                }
            }
        }

    override fun shouldBe(expected: Long): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<List<Int>>(path).let { list ->
                if (list.size == 1 && list[0].toLong() == expected) {
                    Ok
                } else {
                    Failed { listExplain(expected, list) }
                }
            }

        }

    override fun shouldBe(expected: BigDecimal): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<List<Double>>(path).let { list ->
                if (list.size == 1 && list[0]?.toBigDecimal() == expected) {
                    Ok
                } else {
                    Failed { listExplain(expected, list) }
                }
            }
        }

    override fun shouldBe(expected: Boolean): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<List<Boolean>>(path).let { list ->
                if (list.size == 1 && list[0] == expected) {
                    Ok
                } else {
                    Failed { listExplain(expected, list) }
                }
            }

        }

    override fun shouldContain(expected: String): TestResult =
        wrapPathNotFound(expected) {
            JsonPath.parse(json).read<List<String>>(path).let { list ->
                if (list.size == 1 && list[0].contains(expected)) {
                    Ok
                } else {
                    Failed { listExplain(expected, list) }
                }
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

    override fun shouldBeNull(): TestResult =
        wrapPathNotFound(null) {
            JsonPath.parse(json).read<List<Any?>>(path).let { list ->
                if (list.size == 1 && list[0] == null) {
                    Ok
                } else {
                    Failed { listExplain(null, list) }
                }
            }
        }
}