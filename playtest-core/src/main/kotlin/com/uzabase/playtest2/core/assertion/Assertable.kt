package com.uzabase.playtest2.core.assertion

interface AssertableAsString {
    fun asString(): String = this.toString()
}

interface AssertableAsLong {
    fun asLong(): Long = this.toString().toLong()
}