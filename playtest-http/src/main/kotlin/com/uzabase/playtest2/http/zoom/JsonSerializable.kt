package com.uzabase.playtest2.http.zoom

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.uzabase.playtest2.core.assertion.*
import java.math.BigDecimal

fun interface JsonSerializable {
    fun toJsonPathProxy(path: String): JsonPathProxy
}

sealed interface JsonPathProxy : ShouldBeString, ShouldContainsString, ShouldMatchString, ShouldBeLong,
    ShouldBeBigDecimal, ShouldBeBoolean, ShouldBeExist, ShouldNotBeExist {
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

    override fun shouldBe(expected: String): Boolean = JsonPath.parse(json).read<String>(path) == expected
    override fun shouldContain(expected: String): Boolean = JsonPath.parse(json).read<String>(path).contains(expected)
    override fun shouldMatch(expected: String): Boolean =
        expected.toRegex().matches(JsonPath.parse(json).read<String>(path))

    override fun shouldBe(expected: Long): Boolean = JsonPath.parse(json).read<Int>(path).toLong() == expected
    override fun shouldBe(expected: BigDecimal): Boolean =
        JsonPath.parse(json).read<Double>(path).toBigDecimal() == expected

    override fun shouldBe(expected: Boolean): Boolean = JsonPath.parse(json).read<Boolean>(path) == expected
    override fun shouldBeExist(): Boolean =
        try {
            JsonPath.parse(json).read<Any>(path)
            true
        } catch (e: PathNotFoundException) {
            false
        }

    override fun shouldNotBeExist(): Boolean = !shouldBeExist()
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

    override fun shouldBe(expected: Boolean): Boolean =
        JsonPath.parse(json).read<List<Boolean>>(path).let { list ->
            if (list.size == 1) {
                list[0] == expected
            } else {
                throw PlaytestAssertionError("The path is indefinite and the result is not a single value")
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
     * Indefinite なパスの場合、存在しないパスでも必ずリストが返ってくるので、例外の考慮は不要…?
     ***/
    override fun shouldBeExist(): Boolean =
        JsonPath.parse(json).read<List<Any>>(path).isNotEmpty()

    override fun shouldNotBeExist(): Boolean = !shouldBeExist()
}