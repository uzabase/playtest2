package com.uzabase.playtest2.core

import com.thoughtworks.gauge.Table
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.*
import com.uzabase.playtest2.core.proxy.ProxyFactory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

data class FullName(val firstName: String, val lastName: String)

private val FromFullName = { fullName: FullName ->
    object : ShouldBeString, ShouldBeBoolean, ShouldBe<FullName> {
        override fun shouldBe(expected: String): Boolean = "${fullName.firstName} ${fullName.lastName} :)" == expected
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
                shouldThrow<PlaytestAssertionError> {
                    sut.shouldBeLongValue(200L)
                }.message.shouldBe("Assertion target is not found")
            }

            test("string value") {
                shouldThrow<PlaytestAssertionError> {
                    sut.shouldBeStringValue("Hello, world")
                }.message.shouldBe("Assertion target is not found")
            }

            test("true value") {
                shouldThrow<PlaytestAssertionError> { sut.shouldBeTrue() }
                    .message.shouldBe("Assertion target is not found")
            }

            test("false value") {
                shouldThrow<PlaytestAssertionError> { sut.shouldBeFalse() }
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
            shouldThrow<PlaytestAssertionError> {
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
            shouldThrow<PlaytestAssertionError> { sut.shouldBeTrue() }
                .message.shouldBe("should be strict true")
        }

        test("should be false") {
            ScenarioDataStore.put(K.AssertionTarget, "false")
            shouldThrow<PlaytestAssertionError> { sut.shouldBeFalse() }
                .message.shouldBe("should be strict false")
        }
    }

    context("numbers") {
        test("should be true if equal value of BigDecimal") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldBeBigDecimal { expected -> 0.3.toBigDecimal() == expected })
            sut.shouldBeBigDecimal(0.3)
        }

        test("should be fail if not equal value of BigDecimal") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldBeBigDecimal { expected -> 0.3.toBigDecimal() == expected })
            shouldThrow<PlaytestAssertionError> { sut.shouldBeBigDecimal(0.4) }
                .message.shouldBe("should be 0.4")
        }
    }

    context("existence") {
        test("should be true if existed") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldBeExist { true })
            sut.shouldBeExist()
        }

        test("should be fail") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldBeExist { false })
            shouldThrow<PlaytestAssertionError> { sut.shouldBeExist() }
                .message.shouldBe("should be exist")
        }

        test("should be true if not existed") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldNotBeExist { true })
            sut.shouldNotBeExist()
        }

        test("should be fail if exited") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldNotBeExist { false })
            shouldThrow<PlaytestAssertionError> { sut.shouldNotBeExist() }
                .message.shouldBe("should not be exist")
        }
    }

    context("table") {
        test("should be equal") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldBeEqualTable { true })
            sut.shouldBeEqualTable(
                Table(listOf("ans")).apply { addRow(listOf("42")) })
        }

        test("should be fail") {
            ScenarioDataStore.put(K.AssertionTarget, ShouldBeEqualTable { false })
            shouldThrow<PlaytestAssertionError> {
                sut.shouldBeEqualTable(
                    Table(listOf("ans"))
                )
            }.message.shouldBe("should be equal to |ans|\n|---|")
        }
    }
})
