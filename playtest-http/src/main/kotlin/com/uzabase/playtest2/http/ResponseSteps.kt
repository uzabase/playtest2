package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.http.internal.K

internal class PlaytestException(override val message: String) : Exception(message)

internal fun test(message: String, assertExp: () -> Boolean) {
    if (!assertExp()) throw PlaytestException(message)
}

class ResponseSteps {
    @Step("レスポンスステータスコードが<statusCode>である")
    fun assertStatusCode(statusCode: Int) =
            test("Status code should be equal to $statusCode") {
                (ScenarioDataStore.get(K.RESPONSE) as okhttp3.Response).code == statusCode
            }

    @Step("レスポンスヘッダーにキー<key>が存在している")
    fun assertExistsHeaderKey(key: String) =
            test("Header key $key should exist") {
                (ScenarioDataStore.get(K.RESPONSE) as okhttp3.Response).headers(key).isNotEmpty()
            }

}
