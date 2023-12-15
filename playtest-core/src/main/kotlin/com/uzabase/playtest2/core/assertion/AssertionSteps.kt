package com.uzabase.playtest2.core.assertion

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore


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
    fun shouldBeStringValue(value: String) =
        ScenarioDataStore.items().find { it == "AssertionTarget" }?.let {
            val context = ScenarioDataStore.get("Context") as? AssertableAsString
            test("should be $value") {
                if (context != null) {
                    context.asString() == value
                } else {
                    (ScenarioDataStore.get("AssertionTarget") as String) == value
                }
            }
        } ?: playtestException("Assertion target is not found")

}