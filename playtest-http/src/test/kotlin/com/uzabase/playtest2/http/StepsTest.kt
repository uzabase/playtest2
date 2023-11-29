package com.uzabase.playtest2.http

import com.github.tomakehurst.wiremock.WireMockServer
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.Configuration.Companion.playtest2
import com.uzabase.playtest2.config.http
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.net.URI

class StepsTest : FunSpec({
    val sut = Steps()
    val server = WireMockServer(8080)

    beforeSpec {
        playtest2 {
            listOf(http(URI("http://localhost:8080").toURL()))
        }
        server.start()
    }

    beforeEach {
        // Simulate Gauge's ScenarioDataStore being cleared between scenarios
        ScenarioDataStore.items().forEach { k ->
            ScenarioDataStore.remove(k)
        }

        server.resetAll()
    }

    afterSpec() {
        server.stop()
    }

    test("Hello, world") {
        sut.pathIntoRequest("/hello")
        sut.sendGetRequest()
        val response = ScenarioDataStore.get(Steps.KEY.RESPONSE) as okhttp3.Response
        response.body.string().shouldBe("Hello, world!")
    }
})
