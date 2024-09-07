package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.proxy.ProxyFactory
import com.uzabase.playtest2.http.proxy.HttpHeadersProxy
import java.net.http.HttpResponse
import com.uzabase.playtest2.core.K as coreK
import com.uzabase.playtest2.http.internal.K as intK

class FocusResponseSteps {
    @Step("レスポンスのステータスコードが")
    fun statusCode() {
        (ScenarioDataStore.get(intK.RESPONSE) as HttpResponse<*>)
            .let { ProxyFactory.ofLong(it.statusCode().toLong()) }
            .let { ScenarioDataStore.put(coreK.AssertionTarget, it) }
    }

    @Step("レスポンスのボディが")
    fun body() {
        (ScenarioDataStore.get(intK.RESPONSE) as HttpResponse<*>)
            .let { ScenarioDataStore.put(coreK.AssertionTarget, it.body()) }
    }

    @Step("レスポンスのヘッダーが", "レスポンスのヘッダーの")
    fun headers() {
        (ScenarioDataStore.get(intK.RESPONSE) as HttpResponse<*>)
            .let { ScenarioDataStore.put(coreK.AssertionTarget, it.headers().let(HttpHeadersProxy::of)) }
    }
}
