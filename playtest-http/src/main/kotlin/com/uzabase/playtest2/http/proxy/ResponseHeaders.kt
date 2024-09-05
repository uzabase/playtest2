package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.proxy.AssertableProxyFunctions
import com.uzabase.playtest2.core.proxy.ProxyFactory
import com.uzabase.playtest2.core.zoom.Zoomable
import java.net.http.HttpHeaders


fun HttpHeaders.toProxy(): Any =
    this.let { headers: HttpHeaders ->
        ProxyFactory.make<HttpHeaders, String>(
            headers,
            AssertableProxyFunctions(
                asString = {
                    headers.map().entries.sortedBy { (k, _) -> k }
                        .joinToString(System.lineSeparator()) { (k, v) -> "$k: ${v.joinToString(",")}" }
                }
            ),
            zoomable = object : Zoomable<String> {
                override fun zoom(key: String): Any? = headers.map()[key]?.joinToString(",")
            }
        )
    }
