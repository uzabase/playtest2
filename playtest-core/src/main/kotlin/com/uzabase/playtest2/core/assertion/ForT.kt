package com.uzabase.playtest2.core.assertion

fun interface ShouldBe<T> {
    fun shouldBe(expected: T): Boolean
}