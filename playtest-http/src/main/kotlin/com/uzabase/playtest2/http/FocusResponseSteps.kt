package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.ScenarioDataStoreExt
import com.uzabase.playtest2.core.proxy.ProxyFactory
import com.uzabase.playtest2.http.proxy.ResponseBodyProxy
import com.uzabase.playtest2.http.proxy.ResponseHeadersProxy
import com.uzabase.playtest2.http.zoom.JsonSerializable
import java.net.http.HttpResponse
import java.nio.file.Path
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
        ScenarioDataStoreExt.ensureGet<HttpResponse<Path>>(intK.RESPONSE)
            .let { ScenarioDataStore.put(coreK.AssertionTarget, it.body().let(ResponseBodyProxy::of)) }
    }

    @Step("レスポンスのヘッダーが", "レスポンスのヘッダーの")
    fun headers() {
        (ScenarioDataStore.get(intK.RESPONSE) as HttpResponse<*>)
            .let { ScenarioDataStore.put(coreK.AssertionTarget, it.headers().let(ResponseHeadersProxy::of)) }
    }
}
