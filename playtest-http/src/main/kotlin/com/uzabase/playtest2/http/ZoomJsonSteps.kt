package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.ScenarioDataStoreExt
import com.uzabase.playtest2.http.zoom.Json
import com.uzabase.playtest2.http.zoom.JsonSerializable

class ZoomJsonSteps {
    @Step("JSONのパス<jsonPath>に対応する値が")
    fun zoomJson(jsonPath: String): Any =
        ScenarioDataStoreExt.ensureGet<JsonSerializable>(K.AssertionTarget)
            .let {
                it.toJson().let { json ->
                    when (json) {
                        is Json.JsonObject -> json.zoom(jsonPath)
                        is Json.JsonPathProxy -> json
                    }
                }
            }
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
}