package com.uzabase.playtest2.core

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.Assertable
import com.uzabase.playtest2.core.assertion.PlaytestException
import com.uzabase.playtest2.core.assertion.playtestException

internal fun assertable(f: (Assertable<*>) -> Unit) =
    ScenarioDataStore.get(K.AssertionTarget)
        .let { it as? Assertable<*> }
        ?.let(f)
        ?: playtestException("Assertion target is not found")

internal fun test(message: String, assertExp: () -> Boolean) {
    if (!assertExp()) throw PlaytestException(message)
}

class AssertionSteps {

    @Step("整数値の<value>である")
    fun shouldBeLongValue(value: Long) =
        assertable {
            test("should be $value") {
                it.shouldBe(value)
            }
        }

    @Step("文字列の<value>である")
    fun shouldBeStringValue(value: String) =
        assertable {
            test("should be $value") {
                it.shouldBe(value)
            }
        }

    @Step("文字列の<value>を含んでいる")
    fun shouldBeContainsStringValue(value: String) =
        assertable {
            test("should contains $value") {
                it.shouldContain(value)
            }
        }

    @Step("真である")
    fun shouldBeTrue() =
        assertable {
            test("should be strict true") {
                it.shouldBe(true)
            }
        }

    @Step("偽である")
    fun shouldBeFalse() =
        assertable {
            test("should be strict false") {
                it.shouldBe(false)
            }
        }
}