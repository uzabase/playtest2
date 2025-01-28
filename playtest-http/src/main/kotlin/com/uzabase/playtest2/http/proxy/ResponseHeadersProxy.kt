package com.uzabase.playtest2.http.proxy

import com.thoughtworks.gauge.Table
import com.uzabase.playtest2.core.assertion.*
import com.uzabase.playtest2.core.zoom.AsAssociative
import com.uzabase.playtest2.core.zoom.Associative
import com.uzabase.playtest2.core.zoom.TableProxy
import com.uzabase.playtest2.core.zoom.ToTable
import java.net.http.HttpHeaders

class ResponseHeadersProxy private constructor(
    val headers: HttpHeaders
) : ShouldBeString, ShouldContainsString, AsAssociative, ToTable {
    companion object {
        fun of(headers: HttpHeaders): ResponseHeadersProxy {
            return ResponseHeadersProxy(headers)
        }
    }

    // TODO HTTPヘッダー的な文字列表現にするのがいいのか悩んでいる。その場合、あらゆるHTTPヘッダーの仕様を考慮する必要がある。
    override fun toString(): String = headers.map().entries.sortedBy { (k, _) -> k }
        .joinToString(System.lineSeparator()) { (k, v) -> "$k: ${v.joinToString(",")}" }

    override fun shouldBe(expected: String): TestResult =
        if (this.toString() == expected) {
            Ok
        } else {
            Failed { simpleExplain(expected, this.toString()) }
        }

    override fun shouldContain(expected: String): TestResult =
        if (this.toString().contains(expected)) {
            Ok
        } else {
            Failed { simpleExplain(expected, this.toString()) }
        }

    override fun asAssociative(key: String): Associative =
        headers
            .map()
            .map { (k, v) -> k to v.joinToString(",") }
            .toMap()
            .let { Associative.of(it, key) }

    /**
     * TBD 暗黙的にアルファベットオーダーにソートしているのと、 `date` というキーを除外しているのは適切の検討が必要
     */
    override fun toTable(): TableProxy =
        headers
            .map()
            .toList()
            .sortedBy { (k, _) -> k }
            .filter { (k, _) -> k != "date" } // TODO
            .let { l ->
                Table(listOf("key", "value")).apply { l.forEach { (k, v) -> addRow(listOf(k, v.joinToString(","))) } }
            }
            .let { TableProxy(it) }
}