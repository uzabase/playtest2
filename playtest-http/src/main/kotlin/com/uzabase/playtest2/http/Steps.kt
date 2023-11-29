package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.Configuration
import com.uzabase.playtest2.config.HttpModuleConfiguration
import com.uzabase.playtest2.config.HttpModuleKey
import okhttp3.Request

class Steps {

    private val client = okhttp3.OkHttpClient().newBuilder().build()

    private val httpConfig: HttpModuleConfiguration
        get() = Configuration[HttpModuleKey] as HttpModuleConfiguration

    @Step("パス<path>に")
    fun pathIntoRequest(path: String) =
        ScenarioDataStore.put(KEY.REQUEST_PATH, path)

    @Step("GETリクエストを送る")
    fun sendGetRequest() =
        (ScenarioDataStore.get(KEY.REQUEST_PATH) as String)
            .let { httpConfig.endpoint.toURI().resolve(it).toURL() }
            .let { Request.Builder().url(it).build() }
            .let { client.newCall(it).execute() }
            .run { ScenarioDataStore.put(KEY.RESPONSE, this) }

    enum class KEY() {
        REQUEST_PATH,
        RESPONSE
    }
}