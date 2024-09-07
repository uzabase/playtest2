package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.assertion.Assertable
import com.uzabase.playtest2.core.zoom.Zoomable
import java.net.http.HttpHeaders

class HttpHeadersProxy private constructor(val headers: HttpHeaders) : Assertable<HttpHeaders>, Zoomable<String> {
    companion object {
        fun of(headers: HttpHeaders): HttpHeadersProxy {
            return HttpHeadersProxy(headers)
        }
    }

    override fun toString(): String = headers.map().entries.sortedBy { (k, _) -> k }
        .joinToString(System.lineSeparator()) { (k, v) -> "$k: ${v.joinToString(",")}" }

    override fun shouldBe(expected: String): Boolean = this.toString() == expected

    override fun shouldContain(expected: String): Boolean = this.toString().contains(expected)

    override fun shouldBe(expected: HttpHeaders): Boolean = headers == expected

    override fun shouldBe(expected: Long): Boolean = throw UnsupportedOperationException("Cannot convert to Long")
    override fun shouldBe(expected: Boolean): Boolean = throw UnsupportedOperationException("Cannot convert to Boolean")

    override fun zoom(key: String): Any? = headers.map()[key]?.joinToString(",")
}