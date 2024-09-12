package com.uzabase.playtest2.wiremock.proxy

import com.github.tomakehurst.wiremock.client.CountMatchingStrategy
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.uzabase.playtest2.core.assertion.ShouldBeLong

internal fun interface UpdateWireMockRequestParameters {
    fun update(params: WireMockRequestParameters?): WireMockRequestParameters
}

internal data class WireMockRequestParameters(
    val path: String = "",
    val method: String = "",
    val headers: Map<String, List<String>> = emptyMap(),
    val queries: Map<String, List<String>> = emptyMap(),
    val body: String = "",
) {
    companion object {
        fun updateMethodAndName(
            method: String,
            path: String,
        ): UpdateWireMockRequestParameters =
            UpdateWireMockRequestParameters {
                it?.copy(method = method, path = path) ?: WireMockRequestParameters(
                    path,
                    method,
                    emptyMap(),
                    emptyMap()
                )
            }

        fun updateQuery(name: String, value: String): UpdateWireMockRequestParameters =
            /*
            * TODO: クエリ文字列の扱いについて要検討
            *       `?q=a,b,c` と `?q=a&q=b&q=c` のそれぞれについてどう取り扱いたいか
            * */
            UpdateWireMockRequestParameters {
                it?.copy(queries = it.queries + (name to (it.queries[name] ?: emptyList()) + listOf(value)))
                    ?: WireMockRequestParameters(queries = mapOf(name to listOf(value)))
            }

        fun updateHeader(header: String): UpdateWireMockRequestParameters =
            /* TODO: ヘッダーのパースを積極的にやりたくないコロンで名前と値が区切られることを前提にしてパースする
             * c.f. https://www.rfc-editor.org/rfc/rfc2616#page-31
             *      https://www.rfc-editor.org/rfc/rfc7230#section-3.2.4
             */
            header.split(":").map { it.trim() }.let { (name, value) -> updateHeader(name, value) }

        fun updateHeader(name: String, value: String): UpdateWireMockRequestParameters =
            UpdateWireMockRequestParameters {
                it?.copy(headers = it.headers + (name to (it.headers[name] ?: emptyList()) + listOf(value)))
                    ?: WireMockRequestParameters(headers = mapOf(name to listOf(value)))
            }

        fun updateBody(value: String): UpdateWireMockRequestParameters =
            UpdateWireMockRequestParameters {
                it?.copy(body = value) ?: WireMockRequestParameters(body = value)
            }
    }

    fun buildRequest(): RequestPatternBuilder =
        WireMock.requestedFor(method, WireMock.urlPathEqualTo(path))
            .let {
                queries.entries.fold(it) { r, (k, v) ->
                    v.fold(r) { rx, x -> rx.withQueryParam(k, equalTo(x)) }
                }
            }
            .let {
                headers.entries.fold(it) { r, (k, v) ->
                    v.fold(r) { rx, x -> rx.withHeader(k, equalTo(x)) }
                }
            }
            .withRequestBody(equalToJson(body))
}

internal class WireMockProxy private constructor(
    private val mock: WireMock,
    private val params: WireMockRequestParameters?,
) : ShouldBeLong {
    companion object {
        fun of(mock: WireMock) =
            WireMockProxy(mock, null)
    }

    fun update(update: UpdateWireMockRequestParameters): WireMockProxy =
        WireMockProxy(mock, update.update(params))

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