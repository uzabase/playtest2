package com.uzabase.playtest2.core.config

import com.thoughtworks.gauge.datastore.SuiteDataStore
import com.uzabase.playtest2.core.K
import com.uzabase.playtest2.core.assertion.AssertableProxyFactories
import com.uzabase.playtest2.core.assertion.DefaultAssertableProxy
import java.util.concurrent.ConcurrentHashMap

interface ModuleConfiguration

interface ModuleKey

data class ConfigurationEntry(
    val key: ModuleKey,
    val config: ModuleConfiguration,
    val assertableProxies: AssertableProxyFactories = emptyList()
)

operator fun ConfigurationEntry.plus(other: ConfigurationEntry): List<ConfigurationEntry> =
    listOf(this, other)

operator fun List<ConfigurationEntry>.plus(other: ConfigurationEntry): List<ConfigurationEntry> =
    this + listOf(other)

class Configuration private constructor() {
    companion object {
        private val configs: ConcurrentHashMap<ModuleKey, ModuleConfiguration> = ConcurrentHashMap()
        @JvmStatic
        fun playtest2(init: () -> List<ConfigurationEntry>) {
            configs.clear()
            init().forEach {
                configs[it.key] = it.config
            }

            init().fold(DefaultAssertableProxy.defaults) { pxs, c ->
                pxs + c.assertableProxies
            }.run { SuiteDataStore.put(K.AssertableProxyFactories, this) }
        }

        operator fun get(key: ModuleKey): ModuleConfiguration? = configs[key]
    }
}

