package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import okhttp3.ResponseBody

internal data class JsonContainer(
    val json: String,
    val path: String
)

class ZoomJsonSteps {
    @Step("JSONのパス<jsonPath>に対応する値が")
    fun zoomJson(jsonPath: String): Any =
        ScenarioDataStore.get(K.AssertionTarget)
            .let { (it as ResponseBody).string() } // TODO not only for ResponseBody
            .let { ScenarioDataStore.put(K.AssertionTarget, JsonContainer(it, jsonPath)) }

}