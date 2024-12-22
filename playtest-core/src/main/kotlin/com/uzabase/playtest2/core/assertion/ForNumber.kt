package com.uzabase.playtest2.core.assertion

import java.math.BigDecimal

fun interface ShouldBeLong {
    fun shouldBe(expected: Long): TestResult
}

fun interface ShouldBeGreaterEqualLong {
    fun shouldBeGreaterEqual(expected: Long): TestResult
}

fun interface ShouldBeBigDecimal {
    fun shouldBe(expected: BigDecimal): TestResult
}