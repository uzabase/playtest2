package com.uzabase.playtest2.jdbc.config

import com.uzabase.playtest2.core.config.ConfigurationEntry
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf

class JdbcConfigurationTest : FunSpec({

    test("jdbc configuration") {
        jdbc("test", "jdbc:h2:mem:test")
            .shouldBeInstanceOf<ConfigurationEntry>()
    }
})