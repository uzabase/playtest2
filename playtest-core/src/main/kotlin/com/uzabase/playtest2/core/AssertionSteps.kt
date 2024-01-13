package com.uzabase.playtest2.core

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.DefaultAssertableProxy.Companion.defaults
import com.uzabase.playtest2.core.DefaultAssertableProxy.Companion.proxy
import com.uzabase.playtest2.core.assertion.AssertableProxyFactories
import com.uzabase.playtest2.core.assertion.PlaytestException
import com.uzabase.playtest2.core.assertion.playtestException


internal fun test(message: String, assertExp: () -> Boolean) {
    if (!assertExp()) throw PlaytestException(message)
}

internal fun factories() =
    (AnyStore.getAs<AssertableProxyFactories>(K.AssertableProxyFactories) ?: defaults)

class AssertionSteps {
    @Step("整数値の<value>である")
    fun shouldBeLongValue(value: Long) =
        ScenarioDataStore.get("AssertionTarget")?.let {
            proxy(it, factories()) {
                test("should be $value") {
                    it.asLong() == value
                }
            }
        } ?: playtestException("Assertion target is not found")

    @Step("文字列の<value>である")
    fun shouldBeStringValue(value: String) =
        ScenarioDataStore.get("AssertionTarget")?.let {
            proxy(it, factories()) {
                test("should be $value") {
                    it.asString() == value
                }
            }
        } ?: playtestException("Assertion target is not found")
}