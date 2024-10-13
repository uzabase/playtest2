package com.uzabase.playtest2.core

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.*

internal inline fun <reified T> assertable(f: (T) -> Unit) =
    ScenarioDataStore.get(K.AssertionTarget)
        .let { it as? T }
        ?.let(f)
        ?: playtestException("Assertion target is not found")

internal fun test(message: String, assertExp: () -> Boolean) {
    if (!assertExp()) throw PlaytestException(message)
}

class AssertionSteps {

    @Step("整数値の<value>である")
    fun shouldBeLongValue(value: Long) =
        assertable<ShouldBeLong> {
            test("should be $value") {
                it.shouldBe(value)
            }
        }

    @Step("整数値の<value>以上である")
    fun shouldBeGreaterEqualLongValue(value: Long) =
        assertable<ShouldBeGreaterEqualLong> {
            test("should be greater than or equal to $value") {
                it.shouldBeGreaterEqual(value)
            }
        }

    @Step("文字列の<value>である")
    fun shouldBeStringValue(value: String) =
        assertable<ShouldBeString> {
            test("should be $value") {
                it.shouldBe(value)
            }
        }

    @Step("文字列の<value>を含んでいる")
    fun shouldBeContainsStringValue(value: String) =
        assertable<ShouldContainsString> {
            test("should contains $value") {
                it.shouldContain(value)
            }
        }

    @Step("真偽値の<value>である")
    fun shouldBeBooleanValue(value: Boolean) =
        assertable<ShouldBeBoolean> {
            test("should be $value") {
                it.shouldBe(value)
            }
        }

    @Step("真である")
    fun shouldBeTrue() =
        assertable<ShouldBeBoolean> {
            test("should be strict true") {
                it.shouldBe(true)
            }
        }

    @Step("偽である")
    fun shouldBeFalse() =
        assertable<ShouldBeBoolean> {
            test("should be strict false") {
                it.shouldBe(false)
            }
        }
}