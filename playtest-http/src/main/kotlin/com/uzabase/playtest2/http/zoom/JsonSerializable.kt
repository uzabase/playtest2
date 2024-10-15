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
                if(compiled.isDefinite) {
                    DefiniteJsonPathProxy.of(json, compiled)
                } else {
                    TODO()
                }
            }
    }
}

internal class DefiniteJsonPathProxy private constructor(
    private val json: String,
    private val path: JsonPath
) : JsonPathProxy {
    companion object {
        fun of(json: String, path: JsonPath): JsonPathProxy = DefiniteJsonPathProxy(json, path)
    }

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
