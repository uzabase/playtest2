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

                override fun shouldBe(expected: String): Boolean = s == expected
                override fun shouldContain(expected: String): Boolean = s.contains(expected)
                override fun shouldBe(expected: Boolean): TestResult =
                    if (s.toBoolean() == expected) {
                        Ok
                    } else {
                        Failed { simpleExplain(expected, s) }
                    }

                override fun shouldMatch(expected: String): Boolean = expected.toRegex().matches(s)
            }

        fun ofLong(l: Long): Any =
            object : ShouldBeString, ShouldBeLong {
                override fun shouldBe(expected: Long): TestResult = if (l == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, l) }
                }

                override fun shouldBe(expected: String): Boolean = l.toString() == expected
            }

        fun ofBigDecimal(b: BigDecimal): Any =
            ShouldBeBigDecimal { expected -> b == expected }

        fun ofBoolean(b: Boolean): Any =
            object : ShouldBeString, ShouldBeBoolean {
                override fun shouldBe(expected: String): Boolean = b.toString() == expected
                override fun shouldBe(expected: Boolean): TestResult = if (b == expected) {
                    Ok
                } else {
                    Failed { simpleExplain(expected, b) }
                }
            }
    }
}