package com.uzabase.playtest2.http.zoom

import com.jayway.jsonpath.JsonPath
import com.uzabase.playtest2.core.assertion.ShouldBeBoolean
import com.uzabase.playtest2.core.assertion.ShouldBeLong
import com.uzabase.playtest2.core.assertion.ShouldBeString
import com.uzabase.playtest2.core.assertion.ShouldContainsString
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

    class JsonPathProxy internal constructor(json: String, val path: String) : Json(json), ShouldBeString,
        ShouldContainsString, ShouldBeLong, ShouldBeBoolean {
        override fun shouldBe(expected: String): Boolean = JsonPath.read<String>(json, path) == expected

        override fun shouldBe(expected: Long): Boolean = JsonPath.read<Int>(json, path).toLong() == expected

        override fun shouldBe(expected: Boolean): Boolean = JsonPath.read<Boolean>(json, path) == expected

        override fun shouldContain(expected: String): Boolean = JsonPath.read<String>(json, path).contains(expected)
    }
}