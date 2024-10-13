package com.uzabase.playtest2.http.zoom

import com.jayway.jsonpath.JsonPath
import com.uzabase.playtest2.core.assertion.*
import com.uzabase.playtest2.core.zoom.Zoomable

fun interface JsonSerializable {
    fun toJson(): Json
}

sealed class Json(val json: String) {
    companion object {
        fun of(json: String): Json = JsonObject(json)
    }

    class JsonObject internal constructor(json: String) : Json(json), ShouldBeString, Zoomable<String> {
        override fun zoom(key: String): JsonPathProxy = JsonPathProxy(json, key)
        override fun shouldBe(expected: String): Boolean = json == expected
    }

    class JsonPathProxy internal constructor(
        json: String,
        val path: String
    ) : Json(json), ShouldBeString, ShouldContainsString, ShouldMatchString, ShouldBeLong, ShouldBeBoolean {
        override fun shouldBe(expected: String): Boolean = JsonPath.read<String>(json, path) == expected
        override fun shouldContain(expected: String): Boolean = JsonPath.read<String>(json, path).contains(expected)
        override fun shouldMatch(expected: String): Boolean =
            expected.toRegex().matches(JsonPath.read<String>(json, path))

        override fun shouldBe(expected: Long): Boolean = JsonPath.read<Int>(json, path).toLong() == expected
        override fun shouldBe(expected: Boolean): Boolean = JsonPath.read<Boolean>(json, path) == expected

    }
}