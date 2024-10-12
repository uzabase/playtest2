package com.uzabase.playtest2.core

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.zoom.AsAssociative

class ZoomAssociativeSteps {
    @Step("キー<key>に対応する値が")
    fun zoomAssociative(key: String) {
        ScenarioDataStoreExt.ensureGet<AsAssociative>(K.AssertionTarget)
            .asAssociative(key)
            .also { ScenarioDataStore.put(K.AssertionTarget, it) }
    }
}