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
}