package com.uzabase.playtest2.core

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.DefaultAssertableProxy.Companion.defaults
import com.uzabase.playtest2.core.assertion.DefaultAssertableProxy.Companion.proxy
import com.uzabase.playtest2.core.assertion.AssertableProxyFactories
import com.uzabase.playtest2.core.assertion.PlaytestException
import com.uzabase.playtest2.core.assertion.playtestException

internal fun test(message: String, assertExp: () -> Boolean) {
    if (!assertExp()) throw PlaytestException(message)
}

class AssertionSteps {
    private val factories
        get() = (AnyStore.getAs<AssertableProxyFactories>(K.AssertableProxyFactories) ?: defaults)

    @Step("整数値の<value>である")
    fun shouldBeLongValue(value: Long) =
        ScenarioDataStore.get(K.AssertionTarget)?.let {
            proxy(it, factories) {
                test("should be $value") {
                    it.asLong() == value
                }
            }
        } ?: playtestException("Assertion target is not found")

    @Step("文字列の<value>である")
    fun shouldBeStringValue(value: String) =
        ScenarioDataStore.get(K.AssertionTarget)?.let {
            proxy(it, factories) {
                test("should be $value") {
                    it.asString() == value
                }
            }
        } ?: playtestException("Assertion target is not found")

    @Step("文字列の<value>を含んでいる")
    fun shouldBeContainsStringValue(value: String) =
        ScenarioDataStore.get(K.AssertionTarget)?.let {
            proxy(it, factories) {
                test("should contains $value") {
                    it.asString().contains(value)
                }
            }
        } ?: playtestException("Assertion target is not found")

    @Step("真偽値である")
    fun shouldBeBoolean() {
        ScenarioDataStore.get(K.AssertionTarget)
            ?.let { test("should be Boolean") { it is Boolean } }
            ?: playtestException("Assertion target is not found")
    }

    @Step("真である")
    fun shouldBeTrue() {
        ScenarioDataStore.get(K.AssertionTarget)
            ?.let { test("should be strict true") { it == true } }
            ?: playtestException("Assertion target is not found")
    }

    @Step("偽である")
    fun shouldBeFalse() {
        ScenarioDataStore.get(K.AssertionTarget)
            ?.let { test("should be strict false") { it == false } }
            ?: playtestException("Assertion target is not found")
    }
}