package com.uzabase.playtest2.http

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import okhttp3.Headers

class ZoomResponseStepsTest : FunSpec({
    val sut = ZoomResponseSteps()

    beforeEach { ScenarioDataStore.items().forEach { ScenarioDataStore.remove(it) } }

    context("Zooming response") {
        test("should be zoomed") {
            ScenarioDataStore.put(K.AssertionTarget, Headers.headersOf("any-header", "any"))
            sut.zoomMap("any-header")
            ScenarioDataStore.get(K.AssertionTarget).shouldBe("any")
        }

        test("should be removed if key is not found") {
            ScenarioDataStore.put(K.AssertionTarget, Headers.headersOf("any-header", "any"))
            sut.zoomMap("not-found")
            ScenarioDataStore.get(K.AssertionTarget).shouldBeNull()
        }
    }
})
