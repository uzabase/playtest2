package com.uzabase.playtest2.core.proxy

import com.uzabase.playtest2.core.assertion.Assertable
import com.uzabase.playtest2.core.assertion.PlaytestException
import com.uzabase.playtest2.core.assertion.playtestException
import com.uzabase.playtest2.core.zoom.Zoomable
import java.lang.reflect.Proxy

data class AssertableProxyFunctions<T>(
    val asString: (T) -> String = { throw PlaytestException("Cannot convert $it to String") },
    val asLong: (T) -> Long = { throw PlaytestException("Cannot convert $it to Long") },
    val asBoolean: (T) -> Boolean = { throw PlaytestException("Cannot convert $it to Boolean") },
    val asRaw: (T) -> Any = { it as Any }
)

fun <K> unzoomable(): Zoomable<K> = object : Zoomable<K> {
    override fun zoom(key: K): Any {
        throw UnsupportedOperationException("Cannot zoom")
    }

}

@Suppress("UNCHECKED_CAST")
class ProxyFactory {
    companion object {
        inline fun <reified A, K> make(
            obj: A,
            fns: AssertableProxyFunctions<A>,
            zoomable: Zoomable<K> = unzoomable()
        ): Any =
            Proxy.newProxyInstance(
                Assertable::class.java.classLoader,
                arrayOf(
                    Assertable::class.java,
                    Zoomable::class.java
                )
            ) { _, method, args ->
                when (method.name) {
                    "asString" -> fns.asString(obj)
                    "asLong" -> fns.asLong(obj)
                    "asBoolean" -> fns.asBoolean(obj)
                    "asRaw" -> fns.asRaw(obj)
                    "zoom" -> zoomable.zoom(args[0] as K)
                    else -> playtestException("Cannot execute this method")
                }
            }

        fun fromStringValue(s: String): Any = make<String, Unit>(s, AssertableProxyFunctions(
            asString = { s },
            asBoolean = { s.toBoolean() }
        ))

        fun fromLongValue(l: Long): Any = make<Long, Unit>(l, AssertableProxyFunctions(
            asLong = { l }
        ))

        fun fromBooleanValue(b: Boolean) = make<Boolean, Unit>(b, AssertableProxyFunctions(
            asBoolean = { b }
        ))
    }
}