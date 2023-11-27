package com.uzabase.playtest2.wiremock

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore

class Steps {
    @Step("API<apiName>のパス<path>に")
    fun setApiAndPath(apiName: String, path: String) {
        ScenarioDataStore.put(Pair(apiName, KEY.REQUEST_PATH), path)
    }

    @Step("(<apiName>に)GETリクエストされた")
    fun assertRequestedAsGetRequest(apiName: String) {
        val path = ScenarioDataStore.get(Pair(apiName, KEY.REQUEST_PATH)) as String
        val mock = WireMock("localhost", 8080)
        mock.verifyThat(getRequestedFor(urlPathEqualTo(path)))
    }

    @Step("(<apiName>に)GETリクエストされていない")
    fun assertNotRequestedAsGetRequest(apiName: String) {
        val path = ScenarioDataStore.get(Pair(apiName, KEY.REQUEST_PATH)) as String
        val mock = WireMock("localhost", 8080)
        mock.verifyThat(0, getRequestedFor(urlPathEqualTo(path)))
    }

    enum class KEY() {
        REQUEST_PATH
    }
}