package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import okhttp3.Request

class Steps {

    private val client = okhttp3.OkHttpClient().newBuilder().build()

    @Step("パス<path>に")
    fun pathIntoRequest(path: String) {
        ScenarioDataStore.put(KEY.REQUEST_PATH, path)
    }

    @Step("GETリクエストを送る")
    fun sendGetRequest() {
        val path = ScenarioDataStore.get(KEY.REQUEST_PATH) as String
        val request = Request.Builder()
            .url("http://localhost:8080$path")
            .build()
        val response = client.newCall(request).execute()
        ScenarioDataStore.put(KEY.RESPONSE, response)
    }

    enum class KEY() {
        REQUEST_PATH,
        RESPONSE
    }
}