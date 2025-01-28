package com.uzabase.playtest2.core.proxy

import com.uzabase.playtest2.core.assertion.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class ProxyFactoryTest : FunSpec({
    context("Strings") {
        test("ShouldBeString") {
            val proxy = ProxyFactory.ofString("Hello") as ShouldBeString
            proxy.shouldBe("Hello").shouldBe(Ok)
            proxy.shouldBe("World").shouldBeInstanceOf<Failed>()
        }

        test("ShouldContainsString") {
            val proxy = ProxyFactory.ofString("Hello") as ShouldContainsString
            proxy.shouldContain("ell").shouldBe(Ok)
            proxy.shouldContain("abc").shouldBeInstanceOf<Failed>()
        }

        context("ShouldMatch") {
            test("Hello") {
                val proxy = ProxyFactory.ofString("Hello") as ShouldMatchString
                proxy.shouldMatch("H[\\w]{4}").shouldBe(Ok)
                proxy.shouldMatch("H[\\d]{4}").shouldBeInstanceOf<Failed>()
            }

            forAll(
                row("Hello", "H[\\w]{4}"),
                row("Hello, world", "H.{10}d"),
                row("|X20|Y20|", "\\|X\\d{2}\\|Y\\d{2}\\|")
            ) { from, matcher ->
                test("should return Ok: $from $matcher") {
                    val proxy = ProxyFactory.ofString(from) as ShouldMatchString
                    proxy.shouldMatch(matcher).shouldBe(Ok)
                }
            }

            forAll(
                row("Hello", "H[\\d]{4}"),
                row("Hello, world", "H.{10}w"),
            ) { from, matcher ->
                test("should return Failed: $from $matcher") {
                    val proxy = ProxyFactory.ofString(from) as ShouldMatchString
                    proxy.shouldMatch(matcher).shouldBeInstanceOf<Failed>()
                }
            }
        }

        test("ShouldLong") {
            val proxy = ProxyFactory.ofString("123") as ShouldBeLong
            proxy.shouldBe(123).shouldBe(Ok)
            proxy.shouldBe(234).shouldBeInstanceOf<Failed>()
        }

        context("ShouldBoolean") {
            test("true is true") {
                val proxy = ProxyFactory.ofString("true") as ShouldBeBoolean
                proxy.shouldBe(true).shouldBe(Ok)
                when (val actual = proxy.shouldBe(false)) {
                    is Failed -> actual.explain().shouldBeEqual(
                        """
                        Expected:
                          value: false
                          class: kotlin.Boolean
                        Actual:
                          value: true
                          class: kotlin.String
                        """.trimIndent()
                    )

                    else -> throw AssertionError("error")
                }
            }

            test("false is false") {
                val proxy = ProxyFactory.ofString("false") as ShouldBeBoolean
                when (val actual = proxy.shouldBe(true)) {
                    is Failed -> actual.explain().shouldBeEqual(
                        """
                        Expected:
                          value: true
                          class: kotlin.Boolean
                        Actual:
                          value: false
                          class: kotlin.String
                        """.trimIndent()
                    )

                    else -> throw AssertionError("error")
                }
                proxy.shouldBe(false).shouldBe(Ok)
            }

            forAll(
                row("true", true),
                row("false", false),
                row("foo", false),
                row("", false)
            ) { from, expected ->
                val proxy = ProxyFactory.ofString(from) as ShouldBeBoolean
                proxy.shouldBe(expected).shouldBe(Ok)
            }
        }
    }

    context("Boolean") {
        test("should pass only when true") {
            val proxy = ProxyFactory.ofBoolean(true) as ShouldBeBoolean
            proxy.shouldBe(true).shouldBe(Ok)
            when (val actual = proxy.shouldBe(false)) {
                is Failed -> actual.explain().shouldBeEqual(
                    """
                    Expected:
                      value: false
                      class: kotlin.Boolean
                    Actual:
                      value: true
                      class: kotlin.Boolean
                    """.trimIndent()
                )

                else -> throw AssertionError("error")
            }
        }
    }

    context("BigDecimal") {
        test("ShouldBeBigDecimal") {
            val proxy = ProxyFactory.ofBigDecimal(123.45.toBigDecimal()) as ShouldBeBigDecimal
            proxy.shouldBe(123.45.toBigDecimal()).shouldBe(Ok)
            proxy.shouldBe(999.99.toBigDecimal()).shouldBeInstanceOf<Failed>()
        }
    }
})
