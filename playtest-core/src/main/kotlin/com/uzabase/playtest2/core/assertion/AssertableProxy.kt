package com.uzabase.playtest2.core.assertion

import java.lang.reflect.Proxy

data class Proxies<T>(
    val asString: (T) -> String = { throw PlaytestException("Cannot convert $it to String") },
    val asLong: (T) -> Long = { throw PlaytestException("Cannot convert $it to Long") },
    val asBoolean: (T) -> Boolean = { throw PlaytestException("Cannot convert $it to Boolean") },
    val asRaw: (T) -> Any = { it as Any }
)

class AssertableProxy {
    companion object {
        inline fun <reified T> make(obj: T, proxies: Proxies<T>): Any =
            Proxy.newProxyInstance(
                Assertable::class.java.classLoader,
                arrayOf(Assertable::class.java)

            ) { _, method, _ ->
                when (method.name) {
                    "asString" -> proxies.asString(obj)
                    "asLong" -> proxies.asLong(obj)
                    "asBoolean" -> proxies.asBoolean(obj)
                    "asRaw" -> proxies.asRaw(obj)
                    else -> playtestException("Cannot execute this method")
                }
            }

        fun fromStringValue(s: String): Any = make(s, Proxies(
            asString = { s },
            asBoolean = { s.toBoolean() }
        ))

        fun fromLongValue(l: Long): Any = make(l, Proxies(
            asLong = { l }
        ))

        fun fromBooleanValue(b: Boolean) = make(b, Proxies(
            asBoolean = { b }
        ))
    }
}
