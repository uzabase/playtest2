package com.uzabase.playtest2.core.assertion

fun fromLong(x: Any): AssertableProxy? =
    (x as? Long)?.let {
        object : AssertableProxy {
            override val self: Any = x
            override fun asLong(): Long = x
        }
    }


fun fromString(x: Any): AssertableProxy? =
    (x as? String)?.let {
        object : AssertableProxy {
            override val self: Any = x
            override fun asString(): String = x
        }
    }

class DefaultAssertableProxy {
    companion object {
        val defaults: List<AssertableProxyFactory> = listOf(
            ::fromLong,
            ::fromString
        )

        fun proxy(x: Any, factories: List<AssertableProxyFactory> = defaults, body: (AssertableProxy) -> Unit) {
            val assertable = factories.firstNotNullOfOrNull { it(x) }
                ?: throw IllegalArgumentException("Cannot create AssertableProxy from $x")
            body(assertable)
        }
    }
}
