package com.uzabase.playtest2.http.config

import com.uzabase.playtest2.core.config.ConfigurationEntry
import com.uzabase.playtest2.core.config.ModuleConfiguration
import com.uzabase.playtest2.core.config.ModuleKey
import java.net.URL

object HttpModuleKey : ModuleKey
data class HttpModuleConfiguration(val endpoint: URL) : ModuleConfiguration

fun http(endpoint: URL): ConfigurationEntry =
    ConfigurationEntry(HttpModuleKey, HttpModuleConfiguration(endpoint))