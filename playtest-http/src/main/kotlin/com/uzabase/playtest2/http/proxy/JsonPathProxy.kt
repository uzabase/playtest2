package com.uzabase.playtest2.http.proxy

import com.jayway.jsonpath.JsonPath
import com.uzabase.playtest2.core.assertion.Assertable
import java.nio.file.Path

class JsonPathProxy private constructor(val json: String, val path: String) : Assertable<Any> {
    companion object {
        fun of(json: String, path: String): JsonPathProxy {
            return JsonPathProxy(json, path)
        }
        fun of(jsonFilePath: Path, path: String): JsonPathProxy {
            return JsonPathProxy.of(jsonFilePath.toFile().readText(Charsets.UTF_8), path)
        }
    }

    override fun shouldBe(expected: Long): Boolean = JsonPath.read<Long>(json, path) == expected

    override fun shouldBe(expected: String): Boolean = JsonPath.read<String>(json, path) == expected

    override fun shouldContain(expected: String): Boolean = JsonPath.read<String>(json, path).contains(expected)

    override fun shouldBe(expected: Boolean): Boolean = JsonPath.read<Boolean>(json, path) == expected

    override fun shouldBe(expected: Any): Boolean = throw UnsupportedOperationException()   

}