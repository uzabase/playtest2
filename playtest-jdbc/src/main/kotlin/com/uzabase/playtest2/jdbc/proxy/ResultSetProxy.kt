package com.uzabase.playtest2.jdbc.proxy

import com.thoughtworks.gauge.Table
import com.uzabase.playtest2.core.zoom.TableProxy
import com.uzabase.playtest2.core.zoom.ToTable
import java.sql.ResultSet

data class ResultSetProxy internal constructor(
    val results: List<Map<String, Any?>>
) : ToTable {
    companion object {
        fun of(rs: ResultSet): ResultSetProxy {
            val results = mutableListOf<Map<String, Any>>()
            val metadata = rs.metaData
            while (rs.next()) {
                val m = mutableMapOf<String, Any>()
                for (i in 1..metadata.columnCount) {
                    val columnName = metadata.getColumnName(i)
                    val columnType = metadata.getColumnClassName(i).let { Class.forName(it) }
                    val columnValue = rs.getObject(i)

                    m[columnName] =
                        if (columnValue?.javaClass == java.lang.Double::class.java && (columnValue as Double).isNaN()) {
                            columnValue
                        } else {
                            columnType.cast(columnValue)
                        }
                }
                results.add(m)
            }
            return ResultSetProxy(results)
        }
    }

    override fun toTable(): TableProxy =
        results.first().keys.toList().let { columns ->
            TableProxy(Table(columns).apply {
                results.forEach { row ->
                    addRow(columns.map { row[it].toString() })
                }
            })
        }
}