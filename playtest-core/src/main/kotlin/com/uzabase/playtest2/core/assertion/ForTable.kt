package com.uzabase.playtest2.core.assertion

import com.thoughtworks.gauge.Table

fun interface ShouldBeEqualTable {
    fun shouldBeEqual(expected: Table): TestResult
}