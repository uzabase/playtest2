package com.uzabase.playtest2.json

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.json.proxable.AsJsonString
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ZoomJsonStepsTest : FunSpec({
    val sut = ZoomJsonSteps()

    context("Zooming JSON") {
        test("should be zoomed") {
            ScenarioDataStore.put(K.AssertionTarget, object : AsJsonString {
                override fun asJsonString(): String = "{\"key\": \"value\"}"
            })
            sut.zoomJson("$.key")
            ScenarioDataStore.get(K.AssertionTarget).shouldBe(JsonPathContext("{\"key\": \"value\"}", "$.key"))
        }
    }
})
