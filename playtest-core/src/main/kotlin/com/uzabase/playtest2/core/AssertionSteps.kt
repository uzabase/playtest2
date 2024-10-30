package com.uzabase.playtest2.core

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.Table
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.*
import com.uzabase.playtest2.core.zoom.TableProxy
import com.uzabase.playtest2.core.zoom.ToTable

internal inline fun <reified T> assertable(f: (T) -> Unit) =
    ScenarioDataStore.get(K.AssertionTarget)
        .let { it as? T }
        ?.let(f)
        ?: playtestError("Assertion target is not found")

internal fun test(message: String, assertExp: () -> Boolean) {
    if (!assertExp()) throw PlaytestAssertionError(message)
}

class AssertionSteps {
    @Step("小数値の<value>である")
    fun shouldBeBigDecimal(value: Double) =
        assertable<ShouldBeBigDecimal> {
            test("should be $value") {
                it.shouldBe(value.toBigDecimal())
            }
        }

    @Step("整数値の<value>である")
    fun shouldBeLongValue(value: Long) =
        assertable<ShouldBeLong> {
            test("should be $value") {
                it.shouldBe(value)
            }
        }

    @Step("整数値の<value>以上である")
    fun shouldBeGreaterEqualLongValue(value: Long) =
        assertable<ShouldBeGreaterEqualLong> {
            test("should be greater than or equal to $value") {
                it.shouldBeGreaterEqual(value)
            }
        }

    @Step("文字列の<value>である")
    fun shouldBeStringValue(value: String) =
        assertable<ShouldBeString> {
            test("should be $value") {
                it.shouldBe(value)
            }
        }

    @Step("文字列の<value>を含んでいる")
    fun shouldBeContainsStringValue(value: String) =
        assertable<ShouldContainsString> {
            test("should contains $value") {
                it.shouldContain(value)
            }
        }

    @Step("正規表現の<value>に完全一致している")
    fun shouldBeEntireMatchStringValue(value: String) =
        assertable<ShouldMatchString> {
            test("should match $value") {
                it.shouldMatch(value)
            }
        }

    @Step("真偽値の<value>である")
    fun shouldBeBooleanValue(value: Boolean) =
        assertable<ShouldBeBoolean> {
            test("should be $value") {
                it.shouldBe(value)
            }
        }

    @Step("真である")
    fun shouldBeTrue() =
        assertable<ShouldBeBoolean> {
            test("should be strict true") {
                it.shouldBe(true)
            }
        }

    @Step("偽である")
    fun shouldBeFalse() =
        assertable<ShouldBeBoolean> {
            test("should be strict false") {
                it.shouldBe(false)
            }
        }

    @Step("存在している")
    fun shouldBeExist() =
        assertable<ShouldBeExist> {
            test("should be exist") {
                it.shouldBeExist()
            }
        }

    @Step("存在していない")
    fun shouldNotBeExist() =
        assertable<ShouldNotBeExist> {
            test("should not be exist") {
                it.shouldNotBeExist()
            }
        }

    @Step("nullである")
    fun shouldBeNull() =
        assertable<ShouldBeNull> {
            test("should be null") {
                it.shouldBeNull()
            }
        }

    @Step(
        "テーブル<table>である",
        "以下のテーブルである <table>"
    )
    fun shouldBeEqualTable(table: Table) =
        ScenarioDataStore.get(K.AssertionTarget)?.let { target ->
            val sut = when (target) {
                is ToTable -> target.toTable()
                is ShouldBeEqualTable -> target
                else -> throw PlaytestAssertionError("AssertionTarget is not assertable type: ${target.javaClass}")
            }
            test("should be equal to $table") {
                sut.shouldBeEqual(table)
            }
        }
}