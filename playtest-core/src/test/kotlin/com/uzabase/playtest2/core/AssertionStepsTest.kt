package com.uzabase.playtest2.core

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.Assertable
import com.uzabase.playtest2.core.assertion.PlaytestException
import com.uzabase.playtest2.core.proxy.ProxyFactory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

data class FullName(val firstName: String, val lastName: String)

private val FromFullName = { fullName: FullName ->
    object : Assertable<FullName> {
        override fun shouldBe(expected: Long): Boolean = throw UnsupportedOperationException()

        override fun shouldBe(expected: String): Boolean = "${fullName.firstName} ${fullName.lastName} :)" == expected
        override fun shouldContain(expected: String): Boolean = throw UnsupportedOperationException()

        override fun shouldBe(expected: Boolean): Boolean = false

        override fun shouldBe(expected: FullName): Boolean = fullName == expected
    }
}

class AssertionStepsTest : FunSpec({
    val sut = AssertionSteps()
    beforeEach {
        ScenarioDataStore.items().forEach { ScenarioDataStore.remove(it) }
    }

    context("Assertions") {
        context("happy path") {
            test("long value") {
                ScenarioDataStore.put(K.AssertionTarget, ProxyFactory.ofLong(200L))
                sut.shouldBeLongValue(200L)
            }

            test("string value") {
                ScenarioDataStore.put(K.AssertionTarget, ProxyFactory.ofString("Hello, world"))
                sut.shouldBeStringValue("Hello, world")
            }

            test("true") {
                ScenarioDataStore.put(K.AssertionTarget, ProxyFactory.ofBoolean(true))
                sut.shouldBeTrue()
            }

            test("false") {
                ScenarioDataStore.put(K.AssertionTarget, ProxyFactory.ofBoolean(false))
                sut.shouldBeFalse()
            }
        }
    }

    context("failed scenarios") {
        context("assertion target not found") {
            test("long value") {
                shouldThrow<PlaytestException> {
                    sut.shouldBeLongValue(200L)
                }.message.shouldBe("Assertion target is not found")
            }

            test("string value") {
                shouldThrow<PlaytestException> {
                    sut.shouldBeStringValue("Hello, world")
                }.message.shouldBe("Assertion target is not found")
            }

            test("true value") {
                shouldThrow<PlaytestException> { sut.shouldBeTrue() }
                    .message.shouldBe("Assertion target is not found")
            }

            test("false value") {
                shouldThrow<PlaytestException> { sut.shouldBeFalse() }
                    .message.shouldBe("Assertion target is not found")
            }
        }
    }

    context("contains") {
        test("should contains") {
            ScenarioDataStore.put(K.AssertionTarget, ProxyFactory.ofString("Hello, world"))
            sut.shouldBeContainsStringValue("world")
        }

        test("should not contains") {
            ScenarioDataStore.put(K.AssertionTarget, ProxyFactory.ofString("Hello, world"))
            shouldThrow<PlaytestException> {
                sut.shouldBeContainsStringValue("John")
            }.message.shouldBe("should contains John")
        }
    }

    context("proxied value") {
        test("data class") {
            ScenarioDataStore.put(K.AssertionTarget, FromFullName(FullName("Hibiki", "Yuta")))
            sut.shouldBeStringValue("Hibiki Yuta :)")
        }
    }

    xcontext("string representation value is not strict boolean value") {
        test("should be true") {
            ScenarioDataStore.put(K.AssertionTarget, "true")
            shouldThrow<PlaytestException> { sut.shouldBeTrue() }
                .message.shouldBe("should be strict true")
        }

        test("should be false") {
            ScenarioDataStore.put(K.AssertionTarget, "false")
            shouldThrow<PlaytestException> { sut.shouldBeFalse() }
                .message.shouldBe("should be strict false")
        }
    }
})
