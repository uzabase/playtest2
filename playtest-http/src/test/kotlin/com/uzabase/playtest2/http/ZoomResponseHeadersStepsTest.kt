package com.uzabase.playtest2.http

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.assertion.Assertable
import com.uzabase.playtest2.http.proxy.HttpHeadersProxy
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.net.http.HttpHeaders

class ZoomResponseHeadersStepsTest : FunSpec({
    val sut = ZoomResponseHeadersSteps()

    beforeEach { ScenarioDataStore.items().forEach { ScenarioDataStore.remove(it) } }

    context("Zooming response") {
        test("should be zoomed") {
            ScenarioDataStore.put(
                K.AssertionTarget,
                HttpHeaders.of(mapOf("any-header" to listOf("any"))) { _, _ -> true }.let(HttpHeadersProxy::of)
            )
            sut.zoomMap("any-header")
            ScenarioDataStore.get(K.AssertionTarget)
                .let { it as Assertable }
                .asString().shouldBe("any")
        }

        test("should be removed if key is not found") {
            ScenarioDataStore.put(
                K.AssertionTarget,
                HttpHeaders.of(mapOf("any-header" to listOf("any"))) { _, _ -> true }.let(HttpHeadersProxy::of)
            )
            sut.zoomMap("not-found")
            ScenarioDataStore.get(K.AssertionTarget).shouldBeNull()
        }
    }
})
