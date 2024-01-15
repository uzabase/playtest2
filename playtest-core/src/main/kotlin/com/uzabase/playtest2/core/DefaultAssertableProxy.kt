package com.uzabase.playtest2.core

import com.uzabase.playtest2.core.assertion.AssertableProxy
import com.uzabase.playtest2.core.assertion.AssertableProxyFactory
import com.uzabase.playtest2.core.assertion.Proxies
import com.uzabase.playtest2.core.assertion.makeAssertableProxyFactory

val FromString = makeAssertableProxyFactory<String>(Proxies(
    asString = { it },
    asLong = { it.toLong() }
))

val FromLong = makeAssertableProxyFactory<Long>(Proxies(
    asString = { it.toString() },
    asLong = { it }
))

class DefaultAssertableProxy {
    companion object {
        val defaults: List<AssertableProxyFactory> = listOf(
            FromLong,
            FromString
        )

        fun proxy(x: Any, factories: List<AssertableProxyFactory> = defaults, body: (AssertableProxy) -> Unit) {
            val assertable = factories.firstOrNull { it.canProxy(x) }?.create(x)
                ?: throw IllegalArgumentException("Cannot create AssertableProxy from $x")
            body(assertable)
        }
    }
}