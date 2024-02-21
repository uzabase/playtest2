package com.uzabase.playtest2.json

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.json.proxable.AsJsonString


class ZoomJsonSteps {
    @Step("JSONのパス<jsonPath>に対応する値が")
    fun zoomJson(jsonPath: String): Any =
        ScenarioDataStore.get(K.AssertionTarget)
            .let { (it as AsJsonString).asJsonString() }
            .let { ScenarioDataStore.put(K.AssertionTarget, JsonPathContext(it, jsonPath)) }

}