package com.uzabase.playtest2.http.proxy

import com.uzabase.playtest2.core.assertion.ShouldBeString
import com.uzabase.playtest2.core.assertion.ShouldContainsString
import com.uzabase.playtest2.core.zoom.AsAssociative
import com.uzabase.playtest2.core.zoom.Associative
import java.net.http.HttpHeaders

class ResponseHeadersProxy private constructor(
    val headers: HttpHeaders
) : ShouldBeString, ShouldContainsString, AsAssociative {
    companion object {
        fun of(headers: HttpHeaders): ResponseHeadersProxy {
            return ResponseHeadersProxy(headers)
        }
    }

    // TODO HTTPヘッダー的な文字列表現にするのがいいのか悩んでいる。その場合、あらゆるHTTPヘッダーの仕様を考慮する必要がある。
    override fun toString(): String = headers.map().entries.sortedBy { (k, _) -> k }
        .joinToString(System.lineSeparator()) { (k, v) -> "$k: ${v.joinToString(",")}" }

    override fun shouldBe(expected: String): Boolean = this.toString() == expected

    override fun shouldContain(expected: String): Boolean = this.toString().contains(expected)

    override fun asAssociative(key: String): Associative =
        headers
            .map()
            .map { (k, v) -> k to v.joinToString(",") }
            .toMap()
            .let { Associative.of(it, key) }
}