package com.uzabase.playtest2.wiremock.proxy

import com.github.tomakehurst.wiremock.client.CountMatchingStrategy
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.uzabase.playtest2.core.assertion.ShouldBeLong

internal fun interface UpdateWireMockRequestParameters {
    fun update(params: WireMockRequestParameters?): WireMockRequestParameters
}

internal data class WireMockRequestParameters(
    val path: String,
    val method: String,
    val queries: Map<String, List<String>>,
) {
    companion object {
        fun updateMethodAndName(
            method: String,
            path: String,
        ): UpdateWireMockRequestParameters =
            UpdateWireMockRequestParameters {
                it?.copy(method = method, path = path) ?: WireMockRequestParameters(path, method, emptyMap())
            }

        fun updateQuery(name: String, value: String): UpdateWireMockRequestParameters =
            UpdateWireMockRequestParameters {
                it?.copy(queries = it.queries + (name to (it.queries[name] ?: emptyList()) + listOf(value)))
                    ?: WireMockRequestParameters("", "", mapOf(name to listOf(value)))
            }
    }

    fun buildRequest(): RequestPatternBuilder =
        WireMock.requestedFor(method, WireMock.urlPathEqualTo(path))
            .let {
                queries.entries.fold(it) { r, (k, v) ->
                    v.fold(r) { rx, x -> rx.withQueryParam(k, WireMock.equalTo(x)) }
                }
            }
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