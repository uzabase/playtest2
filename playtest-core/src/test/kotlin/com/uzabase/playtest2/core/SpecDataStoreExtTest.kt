package com.uzabase.playtest2.core

import com.thoughtworks.gauge.datastore.SpecDataStore
import com.uzabase.playtest2.core.assertion.PlaytestAssertionError
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith

class SpecDataStoreExtTest : FunSpec({
    beforeEach { SpecDataStore.items().forEach { SpecDataStore.remove(it) } }

    context("getAs") {
        test("should return the value if it exists") {
            SpecDataStore.put("doge", listOf(Doge("space doge")))
            SpecDataStoreExt.getAs<List<Doge>>("doge")
                .shouldBe(listOf(Doge("space doge")))
        }

        test("should return null if the value does not exist") {
            SpecDataStoreExt.getAs<List<Doge>>("doge")
                .shouldBe(null)
        }

        test("should throw PlaytestException if the value is not the expected type") {
            SpecDataStore.put("doge", Doge("space doge"))
            shouldThrow<PlaytestAssertionError> {
                SpecDataStoreExt.getAs<List<Doge>>("doge")
            }.message.shouldStartWith("SpecDataStore does not contain key `doge` as `List`.")
        }
    }
})
