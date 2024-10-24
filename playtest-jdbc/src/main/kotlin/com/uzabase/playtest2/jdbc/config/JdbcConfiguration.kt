package com.uzabase.playtest2.jdbc.config

import com.uzabase.playtest2.core.config.ConfigurationEntry
import com.uzabase.playtest2.core.config.ModuleConfiguration
import com.uzabase.playtest2.core.config.ModuleKey

internal data class JdbcModuleKey(val databaseName: String) : ModuleKey
internal data class JdbcModuleConfiguration(val jdbcUrl: String) : ModuleConfiguration

fun jdbc(databaseName: String, jdbcUrl: String): ConfigurationEntry =
    ConfigurationEntry(
        JdbcModuleKey(databaseName),
        JdbcModuleConfiguration(jdbcUrl)
    )