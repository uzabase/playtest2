package com.uzabase.playtest2.wiremock

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.wiremock.config.WireMockModuleConfiguration
import com.uzabase.playtest2.wiremock.config.WireMockModuleKey

class Steps {

    private fun dataStoreKey(apiName: String, type: KEY): Pair<WireMockModuleKey, KEY> =
        Pair(WireMockModuleKey(apiName), type)

    private fun moduleConfig(apiName: String): WireMockModuleConfiguration =
        Configuration[WireMockModuleKey(apiName)] as WireMockModuleConfiguration

    @Step("API<apiName>のパス<path>に")
    fun setApiAndPath(apiName: String, path: String) {
        ScenarioDataStore.put(dataStoreKey(apiName, KEY.REQUEST_PATH), path)
    }

    @Step("(<apiName>に)GETリクエストされた")
    fun assertRequestedAsGetRequest(apiName: String) {
        val path = ScenarioDataStore.get(dataStoreKey(apiName, KEY.REQUEST_PATH)) as String
        val mock = moduleConfig(apiName).endpoint.let { WireMock(it.host, it.port) }
        mock.verifyThat(getRequestedFor(urlPathEqualTo(path)))
    }

    @Step("(<apiName>に)GETリクエストされていない")
    fun assertNotRequestedAsGetRequest(apiName: String) {
        val path = ScenarioDataStore.get(dataStoreKey(apiName, KEY.REQUEST_PATH)) as String
        val mock = moduleConfig(apiName).endpoint.let { WireMock(it.host, it.port) }
        mock.verifyThat(0, getRequestedFor(urlPathEqualTo(path)))
    }

    enum class KEY() {
        REQUEST_PATH
    }
}