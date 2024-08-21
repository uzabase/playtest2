package com.uzabase.playtest2.core.assertion

class DefaultAssertableProxy {
    companion object {
        val defaults: List<AssertableProxyFactory> = listOf(
            FromLong,
            FromString,
            NonProxy
        )

        fun proxy(x: Any, factories: List<AssertableProxyFactory> = defaults, body: (Assertable) -> Unit) {
            val assertable =
                factories.sortedByDescending(AssertableProxyFactory::priority).firstOrNull { it.canProxy(x) }?.create(x)
                    ?: throw IllegalArgumentException("Cannot create AssertableProxy from $x")
            body(assertable)
        }
    }
}