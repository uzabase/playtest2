package com.uzabase.playtest2.wiremock

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.wiremock.config.WireMockModuleConfiguration
import com.uzabase.playtest2.wiremock.config.WireMockModuleKey
import com.uzabase.playtest2.wiremock.proxy.WireMockProxy
import com.uzabase.playtest2.wiremock.proxy.WireMockRequestParameters.Companion.updateHeader
import com.uzabase.playtest2.wiremock.proxy.WireMockRequestParameters.Companion.updateMethodAndName
import com.uzabase.playtest2.wiremock.proxy.WireMockRequestParameters.Companion.updateQuery


class Steps {
    private fun wireMockConfig(apiName: String): WireMockModuleConfiguration =
        Configuration[WireMockModuleKey(apiName)] as WireMockModuleConfiguration

    @Step("API<apiName>について")
    fun setApi(apiName: String) =
        WireMockProxy.of(wireMockConfig(apiName).toWireMockClient())
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }

    @Step("メソッド<method>でパス<path>に")
    fun initParams(method: String, path: String) =
        (ScenarioDataStore.get(K.AssertionTarget) as WireMockProxy)
            .update(updateMethodAndName(method, path))
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }


    fun query(name: String, value: String) {
        (ScenarioDataStore.get(K.AssertionTarget) as WireMockProxy)
            .update(updateQuery(name, value))
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
    }

    @Step("ヘッダー<header>として")
    fun header(header: String) {
        (ScenarioDataStore.get(K.AssertionTarget) as WireMockProxy)
            .update(updateHeader(header))
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
    }

    @Step("ヘッダー<name>が<value>として")
    fun header(name: String, value: String) {
        (ScenarioDataStore.get(K.AssertionTarget) as WireMockProxy)
            .update(updateHeader(name, value))
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
    }

    @Step("リクエストが送られた回数が")
    fun assertRequestCount() {
        // 日本語表現のためだけの飾り(本質的に必要はない)
    }
}