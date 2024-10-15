package com.uzabase.playtest2.http.zoom

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.uzabase.playtest2.core.assertion.*
import java.math.BigDecimal

fun interface JsonSerializable {
    fun toJsonPathProxy(path: String): JsonPathProxy
}

class JsonPathProxy internal constructor(
    val json: String,
    val path: String
) : ShouldBeString, ShouldContainsString, ShouldMatchString, ShouldBeLong, ShouldBeBigDecimal, ShouldBeBoolean, ShouldBeExist, ShouldNotBeExist {
    override fun shouldBe(expected: String): Boolean = JsonPath.read<String>(json, path) == expected
    override fun shouldContain(expected: String): Boolean = JsonPath.read<String>(json, path).contains(expected)
    override fun shouldMatch(expected: String): Boolean =
        expected.toRegex().matches(JsonPath.read<String>(json, path))

    override fun shouldBe(expected: Long): Boolean = JsonPath.read<Int>(json, path).toLong() == expected
    override fun shouldBe(expected: BigDecimal): Boolean = JsonPath.read<Double>(json, path).toBigDecimal() == expected
    override fun shouldBe(expected: Boolean): Boolean = JsonPath.read<Boolean>(json, path) == expected
    override fun shouldBeExist(): Boolean =
        try {
            JsonPath.read<Any>(json, path)
            true
        } catch (e: PathNotFoundException) {
            false
        }

    override fun shouldNotBeExist(): Boolean = !shouldBeExist()
}