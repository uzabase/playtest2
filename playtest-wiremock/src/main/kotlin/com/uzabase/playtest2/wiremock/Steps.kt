package com.uzabase.playtest2.wiremock

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.wiremock.config.WireMockModuleConfiguration
import com.uzabase.playtest2.wiremock.config.WireMockModuleKey
import com.uzabase.playtest2.wiremock.proxy.RequestPatternBuilderUpdaters.Companion.updateBody
import com.uzabase.playtest2.wiremock.proxy.RequestPatternBuilderUpdaters.Companion.updateHeader
import com.uzabase.playtest2.wiremock.proxy.RequestPatternBuilderUpdaters.Companion.updateJsonPathAndValue
import com.uzabase.playtest2.wiremock.proxy.RequestPatternBuilderUpdaters.Companion.updateQuery
import com.uzabase.playtest2.wiremock.proxy.WireMockProxy


class Steps {
    private fun wireMockConfig(apiName: String): WireMockModuleConfiguration =
        Configuration[WireMockModuleKey(apiName)] as WireMockModuleConfiguration

    @Step("API<apiName>についてメソッド<method>でパス<path>に")
    fun setupApi(apiName: String, method: String, path: String) =
        WireMockProxy.of(wireMockConfig(apiName).toWireMockClient(), method, path)
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }

    @Step("クエリ<name>が<value>として")
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

    @Step("JSONボディ<json>として")
    fun jsonBody(json: String) {
        (ScenarioDataStore.get(K.AssertionTarget) as WireMockProxy)
            .update(updateBody(json))
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
    }

    @Step("JSONパス<jsonPath>に文字列表現の<value>を持つ")
    fun jsonPathAndValue(jsonPath: String, value: String) {
        (ScenarioDataStore.get(K.AssertionTarget) as WireMockProxy)
            .update(updateJsonPathAndValue(jsonPath, value))
            .let { ScenarioDataStore.put(K.AssertionTarget, it) }
    }

    @Step("リクエストが送られた回数が")
    fun assertRequestCount() {
        // 日本語表現のためだけの飾り(本質的に必要はない)
    }
}