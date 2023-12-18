package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.http.config.HttpModuleConfiguration
import com.uzabase.playtest2.http.config.HttpModuleKey
import com.uzabase.playtest2.http.internal.K
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class RequestSteps {

    private val client = okhttp3.OkHttpClient().newBuilder().build()

    private val httpConfig: HttpModuleConfiguration
        get() = Configuration[HttpModuleKey] as HttpModuleConfiguration


    @Step("パス<path>に")
    fun pathIntoRequest(path: String) =
        ScenarioDataStore.put(K.REQUEST_PATH, path)

    @Step("JSONボディ<json>を")
    fun jsonBodyIntoRequest(json: String) =
        ScenarioDataStore.put(K.JSON_BODY, json)

    @Step("メディアタイプ<mediaType>で")
    fun mediaTypeIntoRequest(mediaType: String) =
        ScenarioDataStore.put(K.MEDIA_TYPE, mediaType)

    enum class Method {
        GET,
        DELETE,
        PUT,
        POST,
        PATCH
    }

    @Step("メソッド<method>で")
    fun methodIntoRequest(method: Method) =
        ScenarioDataStore.put(K.METHOD, method)

    @Suppress("UNCHECKED_CAST")
    private fun ensureHeaders(): List<Pair<String, String>> =
        (ScenarioDataStore.get(K.HEADER) as? List<Pair<String, String>>) ?: emptyList()

    @Step("ヘッダー<header>で")
    fun headerIntoRequest(header: String) {
        val headers = ensureHeaders()
        val (key, value) = header.split(":").map(String::trim)
        ScenarioDataStore.put(K.HEADER, headers + (key to value))
    }

    @Step("リクエストを送る")
    fun sendRequest() =
        buildRequest()
            .let { client.newCall(it).execute() }
            .run { ScenarioDataStore.put(K.RESPONSE, this) }

    private fun buildRequest(): Request {
        @Suppress("UNCHECKED_CAST")
        fun <T> ensureGet(key: K): T =
            (ScenarioDataStore.get(key) as? T) ?: throw IllegalStateException(
                """
                You should specify the `${
                    when (key) {
                        K.REQUEST_PATH -> "Request Path"
                        K.METHOD -> "Method"
                        K.MEDIA_TYPE -> "Media Type"
                        else -> "Something wrong..."
                    }
                }`
                """.trimIndent()
            )

        return Request.Builder()
            .url(
                ensureGet<String>(K.REQUEST_PATH)
                    .let(httpConfig.endpoint.toURI()::resolve).toURL()
            )
            .let { rb ->
                ensureHeaders().fold(rb) { b, (k, v) ->
                    b.addHeader(k, v)
                }
            }
            .let { rb ->
                when (val method = ensureGet<Method>(K.METHOD)) {
                    Method.GET -> rb.get()
                    else -> rb.method(
                        method.name,
                        (ScenarioDataStore.get(K.JSON_BODY) as? String)
                            ?.toRequestBody(ensureGet<String>(K.MEDIA_TYPE).toMediaType()) ?: "".toRequestBody()
                    )
                }
            }
            .build()
    }
}