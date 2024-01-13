package com.uzabase.playtest2.core

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.uzabase.playtest2.core.assertion.PlaytestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith

data class Doge(val name: String)

class ScenarioDataStoreExtTest : FunSpec({
    beforeEach { ScenarioDataStore.items().forEach { ScenarioDataStore.remove(it) } }

    context("getAs") {
        test("should return the value if it exists") {
            ScenarioDataStore.put("doge", listOf(Doge("space doge")))
            ScenarioDataStoreExt.getAs<List<Doge>>("doge")
                .shouldBe(listOf(Doge("space doge")))
        }

        test("should return null if the value does not exist") {
            ScenarioDataStoreExt.getAs<List<Doge>>("doge")
                .shouldBeNull()
        }

        test("should throw PlaytestException if the value is not the expected type") {
            ScenarioDataStore.put("doge", Doge("space doge"))
            shouldThrow<PlaytestException> {
                ScenarioDataStoreExt.getAs<List<Doge>>("doge")
            }.message.shouldStartWith("ScenarioDataStore does not contain key `doge` as `List`.")
        }
    }

})
