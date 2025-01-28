package com.uzabase.playtest2.core.assertion

fun interface ShouldBeString {
    fun shouldBe(expected: String): TestResult
}

fun interface ShouldContainsString {
    fun shouldContain(expected: String): TestResult
}

fun interface ShouldMatchString {
    fun shouldMatch(expected: String): TestResult
}