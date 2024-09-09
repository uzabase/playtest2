package com.uzabase.playtest2.wiremock

import com.github.tomakehurst.wiremock.client.CountMatchingStrategy
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.assertion.ShouldBeLong
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.wiremock.config.WireMockModuleConfiguration
import com.uzabase.playtest2.wiremock.config.WireMockModuleKey


internal class WireMockProxy private constructor(
    private val mock: WireMock,
    private val params: WireMockRequestParameters?,
) : ShouldBeLong {

    companion object {
        fun of(mock: WireMock) =
            WireMockProxy(mock, null)
    }

    data class WireMockRequestParameters(
        val path: String,
        val method: String,
    ) {
        fun buildRequest(): RequestPatternBuilder =
            WireMock.requestedFor(method, urlPathEqualTo(path))
    }

    fun initRequest(method: String, path: String) =
        WireMockProxy(mock, WireMockRequestParameters(path, method))

    override fun shouldBe(expected: Long): Boolean =
        try {
            val count = CountMatchingStrategy(CountMatchingStrategy.EQUAL_TO, expected.toInt())
            mock.verifyThat(count, params!!.buildRequest())
            true
        } catch (e: AssertionError) {
            // TODO: たぶんキャッチしない方が良い気がしている
            e.printStackTrace()
            false
        }
}

class Steps {

    private fun dataStoreKey(apiName: String, type: KEY): Pair<WireMockModuleKey, KEY> =
        Pair(WireMockModuleKey(apiName), type)

    private fun wireMockConfig(apiName: String): WireMockModuleConfiguration =
        Configuration[WireMockModuleKey(apiName)] as WireMockModuleConfiguration

    @Step("API<apiName>について")
    fun setApi(apiName: String) =
        WireMockProxy.of(wireMockConfig(apiName).toWireMockClient())
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }

    @Step("メソッド<method>でパス<path>に")
    fun initParams(method: String, path: String) =
        (ScenarioDataStore.get(K.AssertionTarget) as WireMockProxy)
            .initRequest(method, path)
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }

    @Step("リクエストが送られた回数が")
    fun assertRequestCount() {
    }


    @Step("API<apiName>のパス<path>に")
    fun setApiAndPath(apiName: String, path: String) {
        ScenarioDataStore.put(dataStoreKey(apiName, KEY.REQUEST_PATH), path)
    }

    @Step("(<apiName>に)GETリクエストされた")
    fun assertRequestedAsGetRequest(apiName: String) {
        val path = ScenarioDataStore.get(dataStoreKey(apiName, KEY.REQUEST_PATH)) as String
        val mock = wireMockConfig(apiName).endpoint.let { WireMock(it.host, it.port) }
        mock.verifyThat(getRequestedFor(urlPathEqualTo(path)))
    }

    @Step("(<apiName>に)GETリクエストされていない")
    fun assertNotRequestedAsGetRequest(apiName: String) {
        val path = ScenarioDataStore.get(dataStoreKey(apiName, KEY.REQUEST_PATH)) as String
        val mock = wireMockConfig(apiName).endpoint.let { WireMock(it.host, it.port) }
        mock.verifyThat(0, getRequestedFor(urlPathEqualTo(path)))
    }

    enum class KEY() {
        REQUEST_PATH
    }
}