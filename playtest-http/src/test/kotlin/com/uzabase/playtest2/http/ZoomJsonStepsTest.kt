package com.uzabase.playtest2.http

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import okhttp3.ResponseBody.Companion.toResponseBody

class ZoomJsonStepsTest : FunSpec({
    val sut = ZoomJsonSteps()

    context("Zooming JSON") {
        test("should be zoomed") {
            ScenarioDataStore.put(K.AssertionTarget, "{\"key\": \"value\"}".toResponseBody())
            sut.zoomJson("$.key")
            ScenarioDataStore.get(K.AssertionTarget).shouldBe(JsonContainer("{\"key\": \"value\"}", "$.key"))
        }
    }
})
