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

        fun from(x: Any): AssertableProxy =
            from(x, *defaults.toTypedArray())

        fun from(x: Any, vararg factories: AssertableProxyFactory): AssertableProxy =
            listOf(*factories).firstNotNullOfOrNull { it(x) }
                ?: throw IllegalArgumentException("Cannot create AssertableProxy from $x")

    }
}
