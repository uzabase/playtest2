package com.uzabase.playtest2.core.zoom

import com.thoughtworks.gauge.Table
import com.uzabase.playtest2.core.assertion.*

fun interface ToTable {
    fun toTable(): TableProxy
}

data class TableProxy(
    val table: Table
): ShouldBeEqualTable {
    override fun shouldBeEqual(expected: Table): TestResult {
        val failed = mutableListOf<Triple<Int, String, String>>()
        expected.tableRows.forEachIndexed { index, expectedRow ->
            val actualRow = table.tableRows[index]
            expected.columnNames.forEach { col ->
                if (expectedRow.getCell(col) != actualRow.getCell(col)) {
                    failed.add(Triple(index, col, expectedRow.getCell(col)))
                }
            }
        }
        return if (failed.isEmpty()) Ok else Failed { "Table comparison failed: $failed" }
    }
}

