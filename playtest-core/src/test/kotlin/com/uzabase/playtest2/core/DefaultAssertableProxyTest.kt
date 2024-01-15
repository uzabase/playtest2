package com.uzabase.playtest2.core

import com.uzabase.playtest2.core.DefaultAssertableProxy.Companion.proxy
import com.uzabase.playtest2.core.assertion.AssertableProxy
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class DefaultAssertableProxyTest : FunSpec({
    context("testing from") {
        forAll(
            row(200L) { x: AssertableProxy -> x.asLong() shouldBe 200L; null },
            row("Hello, world") { x: AssertableProxy -> x.asString() shouldBe "Hello, world"; null }
        ) { origin, testing ->
            test("should return AssertableProxy when given $origin") {
                proxy(origin) { assertable ->
                    assertable.shouldBeInstanceOf<AssertableProxy>()
                    testing(assertable)
                }
            }
        }

        forAll(
            row(1),
            row(true)
        ) { origin ->
            test("should return null when given $origin") {
                shouldThrow<IllegalArgumentException> {
                    proxy(origin) { _ -> }
                }.message.shouldBe("Cannot create AssertableProxy from $origin")
            }
        }
    }

    context("testing fromLong") {
        test("should return AssertableProxy when given Long") {
            val actual = FromLong.create(200L)
            actual.asLong() shouldBe 200L
        }

//        TODO
//        test("should return null when given String") {
//            val actual = FromLong.create("Hello, world")
//            actual shouldBe null
//        }
    }

    context("testing fromString") {
        test("should return AssertableProxy when given String") {
            val actual = FromString.create("Hello, world")
            actual.asString() shouldBe "Hello, world"
        }

//        TODO
//        test("should return null when given Long") {
//            val actual = FromString.create(200L)
//            actual shouldBe null
//        }
    }
})
