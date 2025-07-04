package com.uzabase.playtest2.core.zoom

import com.thoughtworks.gauge.Table
import com.uzabase.playtest2.core.assertion.Ok
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TableTest : FunSpec({
    test("should true when two tables are equal") {
        TableProxy(Table(listOf("foo", "bar", "baz")).apply {
            addRow(listOf("1", "x", "a"))
            addRow(listOf("2", "y", "b"))
            addRow(listOf("3", "z", "c"))
        }).shouldBeEqual(Table(listOf("foo", "bar", "baz")).apply {
            addRow(listOf("1", "x", "a"))
            addRow(listOf("2", "y", "b"))
            addRow(listOf("3", "z", "c"))
        }).shouldBe(Ok)
    }

    test("should true when two tables are logically equal") {
        TableProxy(Table(listOf("foo", "bar", "baz")).apply {
            addRow(listOf("1", "x", "a"))
            addRow(listOf("2", "y", "b"))
            addRow(listOf("3", "z", "c"))
        }).shouldBeEqual(Table(listOf("foo", "baz", "bar")).apply {
            addRow(listOf("1", "a", "x"))
            addRow(listOf("2", "b", "y"))
            addRow(listOf("3", "c", "z"))
        }).shouldBe(Ok)
    }
})
