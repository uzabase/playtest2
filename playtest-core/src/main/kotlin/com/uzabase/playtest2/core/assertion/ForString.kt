package com.uzabase.playtest2.core.assertion

fun interface ShouldBeString {
    fun shouldBe(expected: String): Boolean
}

fun interface ShouldContainsString {
    fun shouldContain(expected: String): Boolean
}
