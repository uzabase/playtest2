package com.uzabase.playtest2.core.assertion

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