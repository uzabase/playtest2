package com.uzabase.playtest2

import java.util.concurrent.ConcurrentHashMap

interface ModuleConfiguration

interface ModuleKey

data class ConfigurationEntry(
    val key: ModuleKey,
    val config: ModuleConfiguration
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
        }

        operator fun get(key: ModuleKey): ModuleConfiguration? = configs[key]
    }
}

