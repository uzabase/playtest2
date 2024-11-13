package com.uzabase.playtest2.jdbc.config

import com.uzabase.playtest2.core.config.ConfigurationEntry
import com.uzabase.playtest2.core.config.ModuleConfiguration
import com.uzabase.playtest2.core.config.ModuleKey
import java.sql.Connection
import java.sql.DriverManager

internal sealed interface Connector {
    fun connect(): Connection

    companion object {
        fun of(url: String) = JdbcUrl(url)
        fun of(url: String, username: String, password: String) = JdbcUrlWithCredentials(url, username, password)
        fun of(dataSource: javax.sql.DataSource) = DataSource(dataSource)
    }

    data class JdbcUrl(val url: String) : Connector {
        override fun connect(): Connection = DriverManager.getConnection(url)
    }

    data class JdbcUrlWithCredentials(val url: String, val username: String, val password: String) : Connector {
        override fun connect(): Connection = DriverManager.getConnection(url, username, password)
    }

    data class DataSource(val dataSource: javax.sql.DataSource) : Connector {
        override fun connect(): Connection = dataSource.connection
    }
}

internal data class JdbcModuleKey(val databaseName: String) : ModuleKey
internal data class JdbcModuleConfiguration(val connector: Connector) : ModuleConfiguration

fun jdbc(databaseName: String, jdbcUrl: String): ConfigurationEntry =
    ConfigurationEntry(
        JdbcModuleKey(databaseName),
        JdbcModuleConfiguration(Connector.of(jdbcUrl))
    )

fun jdbc(databaseName: String, jdbcUrl: String, username: String, password: String): ConfigurationEntry =
    ConfigurationEntry(
        JdbcModuleKey(databaseName),
        JdbcModuleConfiguration(Connector.of(jdbcUrl, username, password))
    )

fun jdbc(databaseName: String, dataSource: javax.sql.DataSource): ConfigurationEntry =
    ConfigurationEntry(
        JdbcModuleKey(databaseName),
        JdbcModuleConfiguration(Connector.of(dataSource))
    )