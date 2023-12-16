package com.uzabase.playtest2.core.assertion

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class AssertionStepsTest : FunSpec({
    val sut = AssertionSteps()
    beforeEach {
        ScenarioDataStore.items().forEach { ScenarioDataStore.remove(it) }
    }

    context("Assertions") {
        context("happy path") {
            test("long value") {
                ScenarioDataStore.put("AssertionTarget", 200L)
                sut.shouldBeLongValue(200L)
            }

            test("string value") {
                ScenarioDataStore.put("AssertionTarget", "Hello, world")
                sut.shouldBeStringValue("Hello, world")
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
        }
    }


    data class FullName(val firstName: String, val lastName: String) {
        fun proxied(): AssertableAsString =
            object : AssertableAsString {
                override fun asString(): String {
                    return "$firstName $lastName :)"
                }
            }
    }

    context("Any value should assert as string value") {
        forAll(
            row(FullName("John", "Doe").proxied(), "John Doe :)"),
            row("Hello, world", "Hello, world")
        ) { origin, expected ->
            ScenarioDataStore.put("AssertionTarget", origin)

            sut.shouldBeStringValue(expected)
        }
    }
})
