package com.uzabase.playtest2.core.assertion

import java.lang.reflect.Proxy

interface AssertableProxyFactory {
    fun canProxy(x: Any): Boolean
    fun create(x: Any): Assertable

    fun priority(): ProxyPriority
}

typealias AssertableProxyFactories = List<AssertableProxyFactory>

data class Proxies<T>(
    val asString: (T) -> String = { throw PlaytestException("Cannot convert $it to String") },
    val asLong: (T) -> Long = { throw PlaytestException("Cannot convert $it to Long") },
    val asRaw: (T) -> Any = { it as Any }
)

sealed interface ProxyPriority : Comparable<ProxyPriority> {
    data object HIGH : ProxyPriority
    data object MEDIUM : ProxyPriority
    data object LOW : ProxyPriority

    override fun compareTo(other: ProxyPriority): Int =
        when (this) {
            HIGH -> 1
            MEDIUM -> when (other) {
                HIGH -> -1
                MEDIUM -> 0
                LOW -> 1
            }

            LOW -> -1
        }
}

inline fun <reified T> makeAssertableProxyFactory(ps: Proxies<T>, priority: ProxyPriority = ProxyPriority.MEDIUM) =
    object : AssertableProxyFactory {
        override fun canProxy(x: Any): Boolean = x is T
        override fun create(x: Any): Assertable =
            if (x !is T) {
                throw PlaytestException("Cannot create AssertableProxy from `$x`")
            } else {
                Proxy.newProxyInstance(
                    Assertable::class.java.classLoader,
                    arrayOf(Assertable::class.java)
                ) { _, method, _ ->
                    when (method.name) {
                        "asString" -> ps.asString(x as T)
                        "asLong" -> ps.asLong(x as T)
                        "asRaw" -> ps.asRaw(x)
                        else -> throw IllegalArgumentException("Cannot proxy $method")
                    }
                } as Assertable
            }

        override fun priority() = priority
    }
