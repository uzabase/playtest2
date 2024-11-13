package com.uzabase.playtest2.jdbc.config

import com.uzabase.playtest2.core.config.ConfigurationEntry
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

class JdbcConfigurationTest : FunSpec({
    context("jdbc configuration with jdbc url") {
        val entry = jdbc("test", "jdbc:h2:mem:test")
            .shouldBeInstanceOf<ConfigurationEntry>()

        test("should be instance of ConfigurationEntry") {
            entry.shouldBeInstanceOf<ConfigurationEntry>()
        }

        test("should be instance of Connector") {
            val conf = entry.config as JdbcModuleConfiguration
            conf.connector.shouldBeInstanceOf<Connector>()
        }
    }

    context("jdbc configuration with username/password") {
        val entry = jdbc("test", "jdbc:h2:mem:test", "alice", "password")

        test("should be instance of ConfigurationEntry") {
            entry.shouldBeInstanceOf<ConfigurationEntry>()
        }

        test("should be instance of Connector") {
            val conf = entry.config as JdbcModuleConfiguration
            conf.connector.shouldBeInstanceOf<Connector>()
        }
    }

    context("jdbc configuration with DataSource") {
        val entry = jdbc("test", PGSimpleDataSource().apply {
            serverNames = arrayOf("localhost")
            databaseName = "test"
            portNumbers = intArrayOf(5432)
            user = "alice"
            password = "password"
        })

        test("should be instance of ConfigurationEntry") {
            entry.shouldBeInstanceOf<ConfigurationEntry>()
        }

        test("should be instance of Connector") {
            val conf = entry.config as JdbcModuleConfiguration
            conf.connector.shouldBeInstanceOf<Connector>()
        }
    }
})