package com.uzabase.playtest2.core

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.thoughtworks.gauge.datastore.SpecDataStore
import com.thoughtworks.gauge.datastore.SuiteDataStore
import com.uzabase.playtest2.core.assertion.PlaytestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class AnyStoreTest : FunSpec({
    beforeEach {
        ScenarioDataStore.items().forEach { ScenarioDataStore.remove(it) }
        SpecDataStore.items().forEach { SpecDataStore.remove(it) }
        SuiteDataStore.items().forEach { SuiteDataStore.remove(it) }
    }

    context("getAs") {
        forAll(
            row("SuiteDataStore") { k:String, v:Any -> SuiteDataStore.put(k, v) },
            row("SpecDataStore") { k:String, v:Any -> SpecDataStore.put(k, v) },
            row("ScenarioDataStore") { k:String, v:Any -> ScenarioDataStore.put(k, v) }
        ) { name, prepare ->
            test("should return the value if it exists in $name") {
                prepare("doge", listOf(Doge("space doge")))
                AnyStore.getAs<List<Doge>>("doge")
                    .shouldBe(listOf(Doge("space doge")))
            }
        }

        test("should return the value of ScenarioDataStore if it exists both ScenarioDataStore and SpecDataStore") {
            ScenarioDataStore.put("doge", listOf(Doge("space doge")))
            SpecDataStore.put("doge", listOf(Doge("earth doge")))
            AnyStore.getAs<List<Doge>>("doge")
                .shouldBe(listOf(Doge("space doge")))
        }

        test("should return null if the value does not exist") {
            AnyStore.getAs<List<Doge>>("doge")
                .shouldBe(null)
        }
    }
})
