package com.uzabase.playtest2.core.assertion

import java.lang.reflect.Proxy

interface AssertableProxy: AsLong, AsString {
}

interface AssertableProxyFactory {
    fun canProxy(x: Any): Boolean
    fun create(x: Any): AssertableProxy
}

typealias AssertableProxyFactories = List<AssertableProxyFactory>

data class Proxies<T>(
    val asString: (T) -> String = { throw PlaytestException("Cannot convert $it to String") },
    val asLong: (T) -> Long = { throw PlaytestException("Cannot convert $it to Long") }
)

inline fun <reified T> makeAssertableProxyFactory(ps: Proxies<T>) = object : AssertableProxyFactory {
    override fun canProxy(x: Any): Boolean = x is T
    override fun create(x: Any): AssertableProxy =
        Proxy.newProxyInstance(
            AssertableProxy::class.java.classLoader,
            arrayOf(AssertableProxy::class.java)
        ) { _, method, _ ->
            when (method.name) {
                "asString" -> ps.asString(x as T)
                "asLong" -> ps.asLong(x as T)
                else -> throw IllegalArgumentException("Cannot proxy $method")
            }
        } as AssertableProxy
}
