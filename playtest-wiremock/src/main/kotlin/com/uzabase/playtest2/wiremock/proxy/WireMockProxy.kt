package com.uzabase.playtest2.wiremock.proxy

import com.github.tomakehurst.wiremock.client.CountMatchingStrategy
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.uzabase.playtest2.core.assertion.ShouldBeGreaterEqualLong
import com.uzabase.playtest2.core.assertion.ShouldBeLong

internal fun interface IRequestPatternBuilderUpdater {
    fun update(params: RequestPatternBuilder?): RequestPatternBuilder
}

internal class RequestPatternBuilderUpdaters private constructor() {
    companion object {
        fun updateQuery(
            name: String,
            value: String,
        ): IRequestPatternBuilderUpdater =
            IRequestPatternBuilderUpdater {
                if (it == null) {
                    throw IllegalStateException("RequestPatternBuilder is null")
                }
                it.withQueryParam(name, equalTo(value))
            }

        fun updateHeader(header: String): IRequestPatternBuilderUpdater =
            /* TODO: ヘッダーのパースを積極的にやりたくないコロンで名前と値が区切られることを前提にしてパースする
             * c.f. https://www.rfc-editor.org/rfc/rfc2616#page-31
             *      https://www.rfc-editor.org/rfc/rfc7230#section-3.2.4
             */
            header.split(":").map { it.trim() }.let { (name, value) -> updateHeader(name, value) }

        fun updateHeader(name: String, value: String): IRequestPatternBuilderUpdater =
            IRequestPatternBuilderUpdater {
                if (it == null) {
                    throw IllegalStateException("RequestPatternBuilder is null")
                }
                it.withHeader(name, equalTo(value))
            }

        fun updateBody(value: String): IRequestPatternBuilderUpdater =
            IRequestPatternBuilderUpdater {
                if (it == null) {
                    throw IllegalStateException("RequestPatternBuilder is null")
                }
                it.withRequestBody(equalToJson(value))
            }

        fun updateJsonPathAndValue(jsonPath: String, value: String): IRequestPatternBuilderUpdater =
            IRequestPatternBuilderUpdater {
                if (it == null) {
                    throw IllegalStateException("RequestPatternBuilder is null")
                }
                it.withRequestBody(matchingJsonPath(jsonPath, equalTo(value)))
            }
    }
}

internal fun List<IRequestPatternBuilderUpdater>.build(builder: RequestPatternBuilder) =
    this.fold(builder) { r, u -> u.update(r) }

internal class WireMockProxy private constructor(
    private val mock: WireMock,
    private val builder: RequestPatternBuilder,
    private val updaters: List<IRequestPatternBuilderUpdater>,
) : ShouldBeLong, ShouldBeGreaterEqualLong {
    companion object {
        fun of(mock: WireMock, method: String, path: String) =
            WireMockProxy(
                mock,
                requestedFor(method, urlPathEqualTo(path)),
                emptyList()
            )
    }

    fun update(updater: IRequestPatternBuilderUpdater): WireMockProxy =
        WireMockProxy(this.mock, this.builder, this.updaters + listOf(updater))

    override fun shouldBe(expected: Long): Boolean =
        try {
            val count = CountMatchingStrategy(CountMatchingStrategy.EQUAL_TO, expected.toInt())
            mock.verifyThat(count, updaters.build(builder))
            true
        } catch (e: AssertionError) {
            // TODO: たぶんキャッチしない方が良い気がしている
            e.printStackTrace()
            false
        }

    override fun shouldBeGreaterEqual(expected: Long): Boolean =
        try {
            val count = CountMatchingStrategy(CountMatchingStrategy.GREATER_THAN_OR_EQUAL, expected.toInt())
            mock.verifyThat(count, updaters.build(builder))
            true
        } catch (e: AssertionError) {
            e.printStackTrace()
            false
        }
}