package com.uzabase.playtest2.jdbc.proxy

import java.sql.ResultSet

data class ResultSetProxy internal constructor(
    val results: List<Map<String, Any?>>
) {
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
}