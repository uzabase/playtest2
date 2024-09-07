package com.uzabase.playtest2.core.assertion

fun interface ShouldBeLong {
    fun shouldBe(expected: Long): Boolean
}

fun interface ShouldBeString {
    fun shouldBe(expected: String): Boolean
}

fun interface ShouldContainsString {
    fun shouldContain(expected: String): Boolean
}

fun interface ShouldBeBoolean {
    fun shouldBe(expected: Boolean): Boolean
}

fun interface ShouldBe<T> {
    fun shouldBe(expected: T): Boolean
}

interface Assertable<T> : ShouldBeLong, ShouldBeString, ShouldContainsString, ShouldBeBoolean, ShouldBe<T>

