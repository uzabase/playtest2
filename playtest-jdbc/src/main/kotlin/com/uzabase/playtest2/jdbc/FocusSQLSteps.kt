package com.uzabase.playtest2.jdbc

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.jdbc.config.JdbcModuleConfiguration
import com.uzabase.playtest2.jdbc.config.JdbcModuleKey
import com.uzabase.playtest2.jdbc.proxy.ResultSetProxy
import java.sql.DriverManager

internal class FocusSQLSteps {
    private fun jdbcConfig(databaseName: String): JdbcModuleConfiguration =
        Configuration[JdbcModuleKey(databaseName)] as JdbcModuleConfiguration

    @Step("DB<databaseName>にSQL<sql>を実行した結果が")
    fun executeSQL(databaseName: String, sql: String) {
        jdbcConfig(databaseName).connector.connect().use { conn ->
            conn.prepareStatement(sql).executeQuery().use(ResultSetProxy::of)
                .let { ScenarioDataStore.put(K.AssertionTarget, it) }
        }
    }
}