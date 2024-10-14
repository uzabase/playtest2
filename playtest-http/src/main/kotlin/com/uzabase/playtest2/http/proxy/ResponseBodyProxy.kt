package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.assertion.ShouldBeString
import com.uzabase.playtest2.core.assertion.ShouldContainsString
import com.uzabase.playtest2.http.zoom.JsonPathProxy
import com.uzabase.playtest2.http.zoom.JsonSerializable
import java.nio.file.Path

class ResponseBodyProxy private constructor(val body: Path) : ShouldBeString, ShouldContainsString, JsonSerializable {
    companion object {
        fun of(body: Path) = ResponseBodyProxy(body)
    }

    override fun shouldBe(expected: String): Boolean =
        body.toFile().readText(Charsets.UTF_8) == expected

    override fun shouldContain(expected: String): Boolean =
        body.toFile().readText(Charsets.UTF_8).contains(expected)

    override fun toJsonPathProxy(path: String): JsonPathProxy =
        body.toFile().readText(Charsets.UTF_8)
            .let { JsonPathProxy(it, path) }
}