package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.http.proxy.JsonPathProxy
import java.nio.file.Path

class ZoomJsonSteps {
    @Step("JSONのパス<jsonPath>に対応する値が")
    fun zoomJson(jsonPath: String): Any =
        ScenarioDataStore.get(K.AssertionTarget)
            .let {
                when (it) {
                    is Path -> JsonPathProxy.of(it, jsonPath);
                    is String -> JsonPathProxy.of(it, jsonPath);
                    else -> throw IllegalArgumentException("AssertionTarget is not a Path or a String")
                }
            }
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
}