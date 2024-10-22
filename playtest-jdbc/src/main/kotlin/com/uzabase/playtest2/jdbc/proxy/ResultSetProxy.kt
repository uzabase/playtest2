package com.uzabase.playtest2.jdbc.proxy

import java.sql.ResultSet

data class ResultSetProxy internal constructor(
    val results: List<Any>
) {
    companion object {
        fun of(rs: ResultSet): ResultSetProxy {
            val results = mutableListOf<Any>()
            while (rs.next()) {
                results.add(rs.getObject(1))
            }
            return ResultSetProxy(results)
        }
    }
}