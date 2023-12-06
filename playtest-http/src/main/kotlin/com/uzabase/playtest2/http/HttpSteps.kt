package com.uzabase.playtest2.http

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.Configuration
import com.uzabase.playtest2.config.HttpModuleConfiguration
import com.uzabase.playtest2.config.HttpModuleKey
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST

class HttpSteps {

    private val client = okhttp3.OkHttpClient().newBuilder().build()

    private val httpConfig: HttpModuleConfiguration
        get() = Configuration[HttpModuleKey] as HttpModuleConfiguration

    enum class K() {
        REQUEST_PATH,
        JSON_BODY,
        MEDIA_TYPE,
        METHOD,
        RESPONSE
    }

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

    @Step("リクエストを送る")
    fun sendRequest(): Unit =
        buildRequest()
            .let { client.newCall(it).execute() }
            .run { ScenarioDataStore.put(K.RESPONSE, this) }

    private fun buildRequest(): Request {
        @Suppress("UNCHECKED_CAST")
        fun <T> ensureGet(key: K): T =
            ScenarioDataStore.get(key)?.let {
                it as T
            } ?: throw IllegalStateException(
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
                when (val method = ensureGet<Method>(K.METHOD)) {
                    Method.GET -> rb.get()
                    else -> rb.method(
                        method.name,
                        ScenarioDataStore.get(K.JSON_BODY)
                            ?.let { it as String }
                            ?.toRequestBody(ensureGet<String>(K.MEDIA_TYPE).toMediaType()) ?: EMPTY_REQUEST
                    )
                }
            }
            .build()
    }
}