package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.proxy.AssertableProxyFunctions
import com.uzabase.playtest2.core.proxy.ProxyFactory
import com.uzabase.playtest2.core.zoom.Zoomable
import okhttp3.Headers

fun Headers.toProxy(): Any =
    this.let { headers: Headers ->
        ProxyFactory.make<Headers, String>(
            headers,
            AssertableProxyFunctions(
                asString = {
                    headers.sortedBy { (k, _) -> k }
                        .joinToString(System.lineSeparator()) { (k, v) -> "$k: $v" }
                }
            ),
            zoomable = object : Zoomable<String> {
                override fun zoom(key: String): Any? = headers[key]
            }
        )
    }
