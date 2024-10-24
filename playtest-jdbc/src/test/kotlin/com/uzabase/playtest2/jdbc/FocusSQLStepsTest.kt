package com.uzabase.playtest2.jdbc

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.jdbc.config.jdbc
import com.uzabase.playtest2.jdbc.proxy.ResultSetProxy
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import org.testcontainers.containers.PostgreSQLContainer

class FocusSQLStepsTest : FunSpec({

    beforeTest {
        PostgreSQLContainer("postgres:17.0-bookworm")
    }

    test("executeSQL should store result set into scenario data store") {
        Configuration.playtest2 {
            listOf(
                jdbc("Test DB", "jdbc:tc:postgresql:17.0:///databasename?TC_DAEMON=true")
            )
        }
        val steps = FocusSQLSteps()
        steps.executeSQL("Test DB", "select 1 as counted")
        val sut = ScenarioDataStore.get(K.AssertionTarget) as ResultSetProxy
        sut.shouldBeInstanceOf<ResultSetProxy>()
    }
})
