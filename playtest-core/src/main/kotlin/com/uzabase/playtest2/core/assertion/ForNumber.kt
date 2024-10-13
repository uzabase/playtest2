package com.uzabase.playtest2.core.assertion

fun interface ShouldBeLong {
    fun shouldBe(expected: Long): Boolean
}

fun interface ShouldBeGreaterEqualLong {
    fun shouldBeGreaterEqual(expected: Long): Boolean
}
