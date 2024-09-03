package com.uzabase.playtest2.http

import com.jayway.jsonpath.JsonPath
import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.ScenarioDataStoreExt.ensureGet
import com.uzabase.playtest2.core.proxy.ProxyFactory
import com.uzabase.playtest2.core.proxy.AssertableProxyFunctions
import okhttp3.ResponseBody

class ZoomJsonSteps {
    @Step("JSONのパス<jsonPath>に対応する値が")
    fun zoomJson(jsonPath: String): Any =
        ensureGet<ResponseBody>(K.AssertionTarget)
            .let { JsonPath.read<Any>(it.byteStream(), jsonPath) }
            .let { value ->
                ProxyFactory.make<Any, Unit>(value, AssertableProxyFunctions(
                    asString = { value as String },
                    asLong = { value as Long },
                    asBoolean = { value as Boolean },
                    asRaw = { value }
                ))
            }
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
}