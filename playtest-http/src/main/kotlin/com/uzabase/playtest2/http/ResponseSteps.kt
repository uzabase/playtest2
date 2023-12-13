package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.http.internal.K

class PlaytestException(override val message: String) : Exception(message)

class ResponseSteps {

    private fun test(message: String, assertExp: () -> Boolean) {
        if (!assertExp()) throw PlaytestException(message)
    }

    @Step("レスポンスステータスコードが<statusCode>である")
    fun assertStatusCode(statusCode: Int) =
            test("Status code should be equal to $statusCode") {
                (ScenarioDataStore.get(K.RESPONSE) as okhttp3.Response).code == statusCode
            }
}
