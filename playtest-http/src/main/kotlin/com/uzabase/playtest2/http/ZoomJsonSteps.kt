package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.ScenarioDataStoreExt.ensureGet
import com.uzabase.playtest2.http.proxy.JsonPathProxy

class ZoomJsonSteps {
    @Step("JSONのパス<jsonPath>に対応する値が")
    fun zoomJson(jsonPath: String): Any =
        ensureGet<String>(K.AssertionTarget)
            .let { JsonPathProxy.of(it, jsonPath) }
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
}