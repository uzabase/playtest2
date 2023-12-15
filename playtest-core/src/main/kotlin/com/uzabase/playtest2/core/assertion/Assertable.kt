package com.uzabase.playtest2.core.assertion

interface AssertableProxy {
    val self: Any
}

interface AssertableAsString: AssertableProxy {
    fun asString(): String
}

