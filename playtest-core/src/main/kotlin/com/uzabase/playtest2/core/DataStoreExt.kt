package com.uzabase.playtest2.core

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import com.thoughtworks.gauge.datastore.SpecDataStore
import com.thoughtworks.gauge.datastore.SuiteDataStore
import com.uzabase.playtest2.core.assertion.playtestError

object AnyStore {
    inline fun <reified T> getAs(key: Any): T? =
        ScenarioDataStoreExt.getAs(key) ?: SpecDataStoreExt.getAs(key) ?: SuiteDataStoreExt.getAs(key)
}

object ScenarioDataStoreExt {
    inline fun <reified T> getAs(key: Any): T? =
        ScenarioDataStore.get(key)?.let { v ->
            try {
                v as T
            } catch (e: ClassCastException) {
                playtestError(
                    """
                    |ScenarioDataStore does not contain key `$key` as `${T::class.simpleName}`.
                    |Actual:
                    |   type: `${v::class.simpleName}`.
                    |   value: `$v`.
                """.trimMargin()
                )
            }
        }

    inline fun <reified T> ensureGet(key: Any): T =
        if (ScenarioDataStore.items().contains(key)) {
            getAs<T>(key) ?: throw IllegalArgumentException("Cannot get value from ScenarioDataStore with key: $key")
        } else {
            throw IllegalArgumentException("ScenarioDataStore does not contain key: $key")
        }
}

object SpecDataStoreExt {
    inline fun <reified T> getAs(key: Any): T? =
        SpecDataStore.get(key)?.let { v ->
            try {
                v as T
            } catch (e: ClassCastException) {
                playtestError(
                    """
                    |SpecDataStore does not contain key `$key` as `${T::class.simpleName}`.
                    |Actual:
                    |   type: `${v::class.simpleName}`.
                    |   value: `$v`.
                """.trimMargin()
                )
            }
        }
}

object SuiteDataStoreExt {
    inline fun <reified T> getAs(key: Any): T? =
        SuiteDataStore.get(key)?.let { v ->
            try {
                v as T
            } catch (e: ClassCastException) {
                playtestError(
                    """
                    |SuiteDataStore does not contain key `$key` as `${T::class.simpleName}`.
                    |Actual:
                    |   type: `${v::class.simpleName}`.
                    |   value: `$v`.
                """.trimMargin()
                )
            }
        }
}