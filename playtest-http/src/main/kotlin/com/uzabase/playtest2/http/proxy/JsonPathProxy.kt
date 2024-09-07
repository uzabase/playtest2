package com.uzabase.playtest2.http.proxy

import com.jayway.jsonpath.JsonPath
import com.uzabase.playtest2.core.assertion.Assertable

class JsonPathProxy private constructor(val json: String, val path: String) : Assertable {
    companion object {
        fun of(json: String, path: String): JsonPathProxy {
            return JsonPathProxy(json, path)
        }
    }

    override fun asLong(): Long = JsonPath.read(json, path)

    override fun asString(): String = JsonPath.read(json, path)

    override fun asBoolean(): Boolean = JsonPath.read(json, path)

    override fun asRaw(): Any = JsonPath.read(json, path)

}