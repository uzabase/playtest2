package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.http.internal.K
import okhttp3.Response

internal class PlaytestException(override val message: String) : Exception(message)
internal fun playtestException(message: String): Nothing = throw PlaytestException(message)

internal fun test(message: String, assertExp: () -> Boolean) {
    if (!assertExp()) throw PlaytestException(message)
}
object AssertionTarget

class ResponseSteps {
    @Step("レスポンスのステータスコードが")
    fun statusCode() {
        (ScenarioDataStore.get(K.RESPONSE) as Response)
            .let { ScenarioDataStore.put(AssertionTarget, it.code.toLong()) }
    }

    @Step("レスポンスのボディが")
    fun body() {
        (ScenarioDataStore.get(K.RESPONSE) as Response)
            .let { ScenarioDataStore.put(AssertionTarget, it.body) }
    }

    @Step("レスポンスのヘッダーが")
    fun headers() {
        (ScenarioDataStore.get(K.RESPONSE) as Response)
            .let { ScenarioDataStore.put(AssertionTarget, it.headers) }
    }

    @Step("整数値の<value>である")
    fun shouldBeLongValue(value: Long) =
        ScenarioDataStore.items().find { it == AssertionTarget }?.let {
            test("should be $value") {
                (ScenarioDataStore.get(AssertionTarget) as Long) == value
            }
        } ?: playtestException("Assertion target is not found")

    @Step("文字列の<value>である")
    fun shouldBeStringValue(value: String) =
        ScenarioDataStore.items().find { it == AssertionTarget }?.let {
            test("should be $value") {
                (ScenarioDataStore.get(AssertionTarget) as String) == value
            }
        } ?: playtestException("Assertion target is not found")
}
