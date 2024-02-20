package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K

class ZoomResponseSteps {
    @Step("キー<key>に対応する値が")
    fun zoomMap(key: String): Any =
        ScenarioDataStore.get(K.AssertionTarget)
            .let { it as okhttp3.Headers } // TODO: not only for Headers
            .let { it[key] }
            ?.let { ScenarioDataStore.put(K.AssertionTarget, it) }
            // when the key is not found, should remove the previous value
            ?: ScenarioDataStore.remove(K.AssertionTarget)

}