package com.uzabase.playtest2.core.proxy

import com.uzabase.playtest2.core.assertion.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class ProxyFactoryTest : FunSpec({
    context("Strings") {
        test("ShouldBeString") {
            val proxy = ProxyFactory.ofString("Hello") as ShouldBeString
            proxy.shouldBe("Hello").shouldBeTrue()
            proxy.shouldBe("World").shouldBeFalse()
        }

        test("ShouldContainsString") {
            val proxy = ProxyFactory.ofString("Hello") as ShouldContainsString
            proxy.shouldContain("ell").shouldBeTrue()
            proxy.shouldContain("abc").shouldBeFalse()
        }

        context("ShouldMatch") {
            test("Hello") {
                val proxy = ProxyFactory.ofString("Hello") as ShouldMatchString
                proxy.shouldMatch("H[\\w]{4}").shouldBeTrue()
                proxy.shouldMatch("H[\\d]{4}").shouldBeFalse()
            }

            forAll(
                row("Hello", "H[\\w]{4}", true),
                row("Hello", "H[\\d]{4}", false),
                row("Hello, world", "H.{10}d", true),
                row("Hello, world", "H.{10}w", false),
                row("|X20|Y20|", "\\|X\\d{2}\\|Y\\d{2}\\|", true)
            ) { from, matcher, expected ->
                val proxy = ProxyFactory.ofString(from) as ShouldMatchString
                proxy.shouldMatch(matcher).shouldBe(expected)
            }
        }

        test("ShouldLong") {
            val proxy = ProxyFactory.ofString("123") as ShouldBeLong
            proxy.shouldBe(123).shouldBeTrue()
            proxy.shouldBe(234).shouldBeFalse()
        }

        context("ShouldBoolean") {
            test("true is true") {
                val proxy = ProxyFactory.ofString("true") as ShouldBeBoolean
                proxy.shouldBe(true).shouldBeTrue()
                proxy.shouldBe(false).shouldBeFalse()
            }

            test("false is false") {
                val proxy = ProxyFactory.ofString("false") as ShouldBeBoolean
                proxy.shouldBe(true).shouldBeFalse()
                proxy.shouldBe(false).shouldBeTrue()
            }

            forAll(
                row("true", true),
                row("false", false),
                row("foo", false),
                row("", false)
            ) { from, expected ->
                val proxy = ProxyFactory.ofString(from) as ShouldBeBoolean
                proxy.shouldBe(expected).shouldBeTrue()
            }
        }
    }

    context("BigDecimal") {
        test("ShouldBeBigDecimal") {
            val proxy = ProxyFactory.ofBigDecimal(123.45.toBigDecimal()) as ShouldBeBigDecimal
            proxy.shouldBe(123.45.toBigDecimal()).shouldBeTrue()
            proxy.shouldBe(999.99.toBigDecimal()).shouldBeFalse()
        }
    }
})
