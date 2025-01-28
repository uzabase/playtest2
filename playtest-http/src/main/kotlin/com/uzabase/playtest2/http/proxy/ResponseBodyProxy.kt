package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.assertion.*
import com.uzabase.playtest2.http.zoom.JsonPathProxy
import com.uzabase.playtest2.http.zoom.JsonSerializable
import java.nio.file.Path

class ResponseBodyProxy private constructor(val body: Path) : ShouldBeString, ShouldContainsString, JsonSerializable {
    companion object {
        fun of(body: Path) = ResponseBodyProxy(body)
    }

    // FIXME need more explained
    override fun shouldBe(expected: String): TestResult =
        if (body.toFile().readText(Charsets.UTF_8) == expected) {
            Ok
        } else {
            Failed { simpleExplain(expected, body) }
        }

    override fun shouldContain(expected: String): TestResult =
        if (body.toFile().readText(Charsets.UTF_8).contains(expected)) {
            Ok
        } else {
            Failed { simpleExplain(expected, body) }
        }

    override fun toJsonPathProxy(path: String): JsonPathProxy =
        body.toFile().readText(Charsets.UTF_8)
            .let { JsonPathProxy.of(it, path) }
}