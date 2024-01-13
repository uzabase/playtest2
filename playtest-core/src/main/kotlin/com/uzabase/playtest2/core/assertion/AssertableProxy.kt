package com.uzabase.playtest2.core.assertion

interface AssertableProxy :
    AssertableAsLong,
    AssertableAsString {
    val self: Any
}

typealias AssertableProxyFactory = (Any) -> AssertableProxy?
typealias AssertableProxyFactories = List<AssertableProxyFactory>