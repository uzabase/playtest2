package com.uzabase.playtest2.core.assertion

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.DefaultAssertableProxy.Companion.defaults


internal fun test(message: String, assertExp: () -> Boolean) {
    if (!assertExp()) throw PlaytestException(message)
}

class AssertionSteps {
    @Step("整数値の<value>である")
    fun shouldBeLongValue(value: Long) =
        ScenarioDataStore.items().find { it == "AssertionTarget" }?.let {
            test("should be $value") {
                (ScenarioDataStore.get("AssertionTarget") as Long) == value
            }
        } ?: playtestException("Assertion target is not found")

    @Step("文字列の<value>である")
    @SuppressWarnings("UNCHECKED_CAST")
    fun shouldBeStringValue(value: String) =
        (ScenarioDataStore.get("AssertableProxyFactories") as? List<AssertableProxyFactory> ?: defaults)
            .let { factories: List<AssertableProxyFactory> ->
                ScenarioDataStore.items().find { it == "AssertionTarget" }?.let { key ->
                    ScenarioDataStore.get(key).let { t ->
                        test("should be $value") {
                            DefaultAssertableProxy.from(t, *factories.toTypedArray()).asString() == value
                        }
                    }
                } ?: playtestException("Assertion target is not found")
            }
}