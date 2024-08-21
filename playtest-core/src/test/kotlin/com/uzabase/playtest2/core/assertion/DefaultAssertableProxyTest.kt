package com.uzabase.playtest2.core.assertion

import com.uzabase.playtest2.core.assertion.DefaultAssertableProxy.Companion.proxy
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class DefaultAssertableProxyTest : FunSpec({
    context("testing from") {
        forAll(
            row(200L) { x: Assertable -> x.asLong() shouldBe 200L; null },
            row("Hello, world") { x: Assertable -> x.asString() shouldBe "Hello, world"; null },
            row(true) { x: Assertable -> x.asRaw() shouldBe true; null }
        ) { origin, testing ->
            test("should return AssertableProxy when given $origin") {
                proxy(origin) { assertable ->
                    assertable.shouldBeInstanceOf<Assertable>()
                    testing(assertable)
                }
            }
        }
    }

    context("testing fromLong") {
        test("should return AssertableProxy when given Long") {
            val actual = FromLong.create(200L)
            actual.asLong() shouldBe 200L
        }

        test("should throw PlaytestException when given String") {
            shouldThrow<PlaytestException> {
                FromLong.create("Hello, world")
            }.message shouldBe "Cannot create AssertableProxy from `Hello, world`"
        }
    }

    context("testing fromString") {
        test("should return AssertableProxy when given String") {
            val actual = FromString.create("Hello, world")
            actual.asString() shouldBe "Hello, world"
        }

        test("should throw PlaytestException when given Long") {
            shouldThrow<PlaytestException> {
                FromString.create(200L)
            }.message shouldBe "Cannot create AssertableProxy from `200`"
        }
    }

    context("from any") {
        test("should return AssertableProxy when given boolean") {
            val actual = NonProxy.create(true)
            actual.asRaw() shouldBe true
        }

        test("should return AssertableProxy when given String") {
            val actual = NonProxy.create("Hello, world")
            actual.asRaw() shouldBe "Hello, world"
        }

        test("should return AssertableProxy when given Long") {
            val actual = NonProxy.create(200L)
            actual.asRaw() shouldBe 200L
        }
    }
})
