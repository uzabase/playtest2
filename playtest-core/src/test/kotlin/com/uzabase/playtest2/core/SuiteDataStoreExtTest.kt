package com.uzabase.playtest2.core

import com.thoughtworks.gauge.datastore.SuiteDataStore
import com.uzabase.playtest2.core.assertion.PlaytestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith

class SuiteDataStoreExtTest : FunSpec({
    beforeEach { SuiteDataStore.items().forEach { SuiteDataStore.remove(it) } }

    context("getAs") {
        test("should return the value if it exists") {
            SuiteDataStore.put("doge", listOf(Doge("space doge")))
            SuiteDataStoreExt.getAs<List<Doge>>("doge")
                .shouldBe(listOf(Doge("space doge")))
        }

        test("should return null if the value does not exist") {
            SuiteDataStoreExt.getAs<List<Doge>>("doge")
                .shouldBeNull()
        }

        test("should throw PlaytestException if the value is not the expected type") {
            SuiteDataStore.put("doge", Doge("space doge"))
            shouldThrow<PlaytestException> {
                SuiteDataStoreExt.getAs<List<Doge>>("doge")
            }.message.shouldStartWith("SuiteDataStore does not contain key `doge` as `List`.")
        }
    }
})
