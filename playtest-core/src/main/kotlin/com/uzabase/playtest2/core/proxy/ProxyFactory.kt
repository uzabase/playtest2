package com.uzabase.playtest2.core.proxy

import com.uzabase.playtest2.core.assertion.*
import java.math.BigDecimal

class ProxyFactory {
    companion object {
        fun ofString(s: String): Any =
            object : ShouldBeString, ShouldContainsString, ShouldBeLong, ShouldBeBoolean, ShouldMatchString {
                override fun shouldBe(expected: Long): TestResult = if (s.toLong() == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, s) }
                }

                override fun shouldBe(expected: String): TestResult = if (s == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, s) }
                }

                override fun shouldContain(expected: String): TestResult = if (s.contains(expected)) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, s) }
                }

                override fun shouldBe(expected: Boolean): TestResult =
                    if (s.toBoolean() == expected) {
                        Ok
                    } else {
                        Failed { simpleExplain(expected, s) }
                    }

                override fun shouldMatch(expected: String): TestResult =
                    if (expected.toRegex().matches(s)) {
                        Ok
                    } else {
                        Failed { simpleExplain(expected, s) }
                    }
            }

        fun ofLong(l: Long): Any =
            object : ShouldBeString, ShouldBeLong {
                override fun shouldBe(expected: Long): TestResult = if (l == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, l) }
                }

                override fun shouldBe(expected: String): TestResult = if (l.toString() == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, l) }
                }
            }

        fun ofBigDecimal(b: BigDecimal): Any =
            ShouldBeBigDecimal { expected ->
                if (b == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, b) }
                }
            }

        fun ofBoolean(b: Boolean): Any =
            object : ShouldBeString, ShouldBeBoolean {
                override fun shouldBe(expected: String): TestResult = if (b.toString() == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, b) }
                }

                override fun shouldBe(expected: Boolean): TestResult = if (b == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, b) }
                }
            }
    }
}