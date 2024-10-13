package com.uzabase.playtest2.core.proxy

import com.uzabase.playtest2.core.assertion.*

class ProxyFactory {
    companion object {
        fun ofString(s: String): Any =
            object : ShouldBeString, ShouldContainsString, ShouldBeLong, ShouldBeBoolean {
                override fun shouldBe(expected: Long): Boolean = s.toLong() == expected
                override fun shouldBe(expected: String): Boolean = s == expected
                override fun shouldContain(expected: String): Boolean = s.contains(expected)
                override fun shouldBe(expected: Boolean): Boolean = s.toBoolean()
            }

        fun ofLong(l: Long): Any =
            object : ShouldBeString, ShouldBeLong {
                override fun shouldBe(expected: Long): Boolean = l == expected
                override fun shouldBe(expected: String): Boolean = l.toString() == expected
            }

        fun ofBoolean(b: Boolean): Any =
            object : ShouldBeString, ShouldBeBoolean {
                override fun shouldBe(expected: String): Boolean = b.toString() == expected
                override fun shouldBe(expected: Boolean): Boolean = b == expected
            }
    }
}