package com.uzabase.playtest2.core.assertion

interface AssertableProxy : AssertableAsString {
    val self: Any
}

interface AssertableAsString {
    fun asString(): String

    companion object {
        fun of(x: Any): AssertableAsString =
            when (x) {
                is AssertableAsString -> x
                else -> object : AssertableAsString {
                    override fun asString(): String = x.toString()
                }
            }
    }
}