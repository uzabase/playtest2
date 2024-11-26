package com.uzabase.playtest2.core.assertion

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class AssertableTest : FunSpec({

    context("Assertable") {
        afterEach { ScenarioDataStore.remove(K.AssertionTarget) }

        test("assertable takes lambda that is take an argument as T") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldBeLong { true })
            assertable<ShouldBeLong> { it.shouldBeInstanceOf<ShouldBeLong>() }
        }

        test("assertable throws PlaytestAssertionError when AssertionTarget is missing") {
            shouldThrow<PlaytestAssertionError> {
                assertable<ShouldBeLong> { it.shouldBeInstanceOf<ShouldBeLong>() }
            }.message.shouldBe("Assertion Target is missing.\nExpected: class com.uzabase.playtest2.core.assertion.ShouldBeLong")
        }
    }

    context("Test") {
        test("should not do anything when test expression returns `Ok`") {
            test { Ok }
        }

        test("should fail ") {
            shouldThrow<PlaytestAssertionError> {
                test { Failed { "expected value is `1` but actual value is `42`" } }
            }.message.shouldBe("expected value is `1` but actual value is `42`")
        }
    }

    context("defaultExplain") {
        test("should return expected and actual values") {
            simpleExplain(1, 42).shouldBe("""
                Expected:
                  value: 1
                  class: kotlin.Int
                Actual:
                  value: 42
                  class: kotlin.Int""".trimIndent())
        }
    }
})
