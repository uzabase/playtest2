package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.http.internal.K
import okhttp3.Response

class FocusResponseSteps {
    @Step("レスポンスのステータスコードが")
    fun statusCode() {
        (ScenarioDataStore.get(K.RESPONSE) as Response)
            .let { ScenarioDataStore.put("AssertionTarget", it.code.toLong()) }
    }

    @Step("レスポンスのボディが")
    fun body() {
        (ScenarioDataStore.get(K.RESPONSE) as Response)
            .let { ScenarioDataStore.put("AssertionTarget", it.body) }
    }

    @Step("レスポンスのヘッダーが", "レスポンスのヘッダーの")
    fun headers() {
        (ScenarioDataStore.get(K.RESPONSE) as Response)
            .let { ScenarioDataStore.put("AssertionTarget", it.headers) }
    }
}
