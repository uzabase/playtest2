package com.uzabase.playtest2.core.assertion

fun interface ShouldBeBoolean {
    fun shouldBe(expected: Boolean): TestResult
}