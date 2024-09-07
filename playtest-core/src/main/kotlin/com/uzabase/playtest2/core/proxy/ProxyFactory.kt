package com.uzabase.playtest2.core.proxy

import com.uzabase.playtest2.core.assertion.Assertable

class ProxyFactory {
    companion object {
        fun ofString(s: String): Assertable<String> =
            object : Assertable<String> {
                override fun shouldBe(expected: Long): Boolean = s.toLong() == expected
                override fun shouldBe(expected: String): Boolean = s == expected
                override fun shouldContain(expected: String): Boolean = s.contains(expected)
                override fun shouldBe(expected: Boolean): Boolean = s.toBoolean()
            }

        fun ofLong(l: Long): Assertable<Long> =
            object : Assertable<Long> {
                override fun shouldBe(expected: Long): Boolean = l == expected
                override fun shouldBe(expected: String): Boolean = l.toString() == expected
                override fun shouldContain(expected: String): Boolean = throw UnsupportedOperationException()
                override fun shouldBe(expected: Boolean): Boolean = throw UnsupportedOperationException()
            }

        fun ofBoolean(b: Boolean): Assertable<Boolean> =
            object : Assertable<Boolean> {
                override fun shouldBe(expected: Long): Boolean = throw UnsupportedOperationException()
                override fun shouldBe(expected: String): Boolean = b.toString() == expected
                override fun shouldContain(expected: String): Boolean = throw UnsupportedOperationException()
                override fun shouldBe(expected: Boolean): Boolean = b == expected
            }
    }
}