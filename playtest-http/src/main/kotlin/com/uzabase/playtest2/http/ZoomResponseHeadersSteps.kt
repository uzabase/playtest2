package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.ScenarioDataStoreExt
import com.uzabase.playtest2.core.proxy.ProxyFactory
import com.uzabase.playtest2.core.zoom.Zoomable

class ZoomResponseHeadersSteps {
    @Step("キー<key>に対応する値が")
    fun zoomMap(key: String): Any =
        ScenarioDataStoreExt.ensureGet<Zoomable<String>>(K.AssertionTarget)
            .zoom(key)
            ?.let { ScenarioDataStore.put(K.AssertionTarget, ProxyFactory.ofString(it as String)) }
            ?: ScenarioDataStore.remove(K.AssertionTarget)
}