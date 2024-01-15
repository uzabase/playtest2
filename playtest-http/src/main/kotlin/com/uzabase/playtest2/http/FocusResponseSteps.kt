package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import okhttp3.Response
import com.uzabase.playtest2.core.K as coreK
import com.uzabase.playtest2.http.internal.K as intK

class FocusResponseSteps {
    @Step("レスポンスのステータスコードが")
    fun statusCode() {
        (ScenarioDataStore.get(intK.RESPONSE) as Response)
            .let { ScenarioDataStore.put(coreK.AssertionTarget, it.code.toLong()) }
    }

    @Step("レスポンスのボディが")
    fun body() {
        (ScenarioDataStore.get(intK.RESPONSE) as Response)
            .let { ScenarioDataStore.put(coreK.AssertionTarget, it.body) }
    }

    @Step("レスポンスのヘッダーが", "レスポンスのヘッダーの")
    fun headers() {
        (ScenarioDataStore.get(intK.RESPONSE) as Response)
            .let { ScenarioDataStore.put(coreK.AssertionTarget, it.headers) }
    }
}
