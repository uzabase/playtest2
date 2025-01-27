package com.uzabase.playtest2.core.assertion

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K

inline fun <reified T> assertable(f: (T) -> Unit): Unit =
    (ScenarioDataStore.get(K.AssertionTarget) as? T)?.let(f) ?: throw PlaytestAssertionError(
        """
        |Assertion Target is missing.
        |Expected: ${T::class}
        """.trimMargin()
    )

sealed interface TestResult
data object Ok : TestResult
fun interface Failed : TestResult {
    fun explain(): String
}

fun simpleExplain(expected: Any?, actual: Any?): String =
    """
    |Expected:
    |  value: $expected
    |  class: ${
        if (expected == null) {
            "null"
        } else {
            expected::class.qualifiedName
        }
    }
    |Actual:
    |  value: $actual
    |  class: ${
        if (actual == null) {
            "null"
        } else {
            actual::class.qualifiedName
        }
    }
    """.trimMargin()

fun test(expr: () -> TestResult): Unit =
    when (val x = expr()) {
        Ok -> Unit
        is Failed -> throw PlaytestAssertionError(x.explain())
    }
