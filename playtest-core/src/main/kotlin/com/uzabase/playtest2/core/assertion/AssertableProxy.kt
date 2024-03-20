package com.uzabase.playtest2.core.assertion

import java.lang.reflect.Proxy

interface AssertableProxy : AsLong, AsString, AsRaw

interface AssertableProxyFactory {
    fun canProxy(x: Any): Boolean
    fun create(x: Any): AssertableProxy

    fun priority(): ProxyPriority
}

typealias AssertableProxyFactories = List<AssertableProxyFactory>

data class Proxies<T>(
    val asString: (T) -> String = { throw PlaytestException("Cannot convert $it to String") },
    val asLong: (T) -> Long = { throw PlaytestException("Cannot convert $it to Long") },
    val asRaw: (T) -> Any = { it as Any }
)

enum class ProxyPriority(val value: Int) {
    HIGH(1), MEDIUM(0), LOW(-1)
}

inline fun <reified T> makeAssertableProxyFactory(ps: Proxies<T>, priority: ProxyPriority = ProxyPriority.MEDIUM) = object : AssertableProxyFactory {
    override fun canProxy(x: Any): Boolean = x is T
    override fun create(x: Any): AssertableProxy =
        if (x !is T) {
            throw PlaytestException("Cannot create AssertableProxy from `$x`")
        } else {
            Proxy.newProxyInstance(
                AssertableProxy::class.java.classLoader,
                arrayOf(AssertableProxy::class.java)
            ) { _, method, _ ->
                when (method.name) {
                    "asString" -> ps.asString(x as T)
                    "asLong" -> ps.asLong(x as T)
                    "asRaw" -> ps.asRaw(x)
                    else -> throw IllegalArgumentException("Cannot proxy $method")
                }
            } as AssertableProxy
        }

    override fun priority() = priority
}
