package com.uzabase.playtest2.core

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.DefaultAssertableProxy.Companion.defaults
import com.uzabase.playtest2.core.assertion.PlaytestException
import com.uzabase.playtest2.core.assertion.Proxies
import com.uzabase.playtest2.core.assertion.makeAssertableProxyFactory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

data class FullName(val firstName: String, val lastName: String)

val FromFullName = makeAssertableProxyFactory<FullName>(Proxies(
    asString = { "${it.firstName} ${it.lastName} :)" }
))

class AssertionStepsTest : FunSpec({
    val sut = AssertionSteps()
    beforeEach {
        ScenarioDataStore.items().forEach { ScenarioDataStore.remove(it) }
    }

    context("Assertions") {
        context("happy path") {
            test("long value") {
                ScenarioDataStore.put(K.AssertionTarget, 200L)
                sut.shouldBeLongValue(200L)
            }

            test("string value") {
                ScenarioDataStore.put(K.AssertionTarget, "Hello, world")
                sut.shouldBeStringValue("Hello, world")
            }

            test("strict bool value") {
                ScenarioDataStore.put(K.AssertionTarget, true)
                sut.shouldBeBoolean()
            }

            test("strict true") {
                ScenarioDataStore.put(K.AssertionTarget, true)
                sut.shouldBeTrue()
            }

            test("strict false") {
                ScenarioDataStore.put(K.AssertionTarget, false)
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

            test("strict bool assertion target is missing") {
                shouldThrow<PlaytestException> { sut.shouldBeBoolean() }
                    .message.shouldBe("Assertion target is not found")
            }

            test("strict true assertion target is missing") {
                shouldThrow<PlaytestException> { sut.shouldBeTrue() }
                    .message.shouldBe("Assertion target is not found")
            }

            test("strict false assertion target is missing") {
                shouldThrow<PlaytestException> { sut.shouldBeFalse() }
                    .message.shouldBe("Assertion target is not found")
            }
        }
    }

    context("Any value should assert as string value") {
        forAll(
            row(FullName("John", "Doe"), listOf(*defaults.toTypedArray(), FromFullName), "John Doe :)"),
            row("Hello, world", defaults, "Hello, world")
        ) { origin, factories, expected ->
            ScenarioDataStore.put(K.AssertableProxyFactories, factories)
            ScenarioDataStore.put(K.AssertionTarget, origin)

            sut.shouldBeStringValue(expected)
        }
    }

    context("contains") {
        test("should contains") {
            ScenarioDataStore.put(K.AssertionTarget, "Hello, world")
            sut.shouldBeContainsStringValue("world")
        }

        test("should not contains") {
            ScenarioDataStore.put(K.AssertionTarget, "Hello, world")
            shouldThrow<PlaytestException> {
                sut.shouldBeContainsStringValue("John")
            }.message.shouldBe("should contains John")
        }
    }

    context("string representation value is not strict boolean value") {
        test( "should be boolean") {
            ScenarioDataStore.put(K.AssertionTarget, "true")
            shouldThrow<PlaytestException> { sut.shouldBeBoolean() }
                .message.shouldBe("should be Boolean. but was java.lang.String")
        }

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
