package com.uzabase.playtest2.core.assertion

interface AssertableProxy: AssertableAsString {
    val self: Any
}

interface AssertableAsString {
    fun asString(): String
}