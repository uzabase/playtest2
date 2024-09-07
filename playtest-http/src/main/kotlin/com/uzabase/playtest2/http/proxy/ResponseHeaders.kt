package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.assertion.Assertable
import com.uzabase.playtest2.core.zoom.Zoomable
import java.net.http.HttpHeaders

class HttpHeadersProxy private constructor(val headers: HttpHeaders) : Assertable, Zoomable<String> {
    companion object {
        fun of(headers: HttpHeaders): HttpHeadersProxy {
            return HttpHeadersProxy(headers)
        }
    }

    override fun asString(): String =
        headers.map().entries.sortedBy { (k, _) -> k }
            .joinToString(System.lineSeparator()) { (k, v) -> "$k: ${v.joinToString(",")}" }

    override fun asRaw(): HttpHeaders = headers

    override fun asLong(): Long = throw UnsupportedOperationException("Cannot convert to Long")
    override fun asBoolean(): Boolean = throw UnsupportedOperationException("Cannot convert to Boolean")

    override fun zoom(key: String): Any? = headers.map()[key]?.joinToString(",")
}