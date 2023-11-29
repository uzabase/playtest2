package com.uzabase.playtest2.config

import com.uzabase.playtest2.ConfigurationEntry
import com.uzabase.playtest2.ModuleConfiguration
import com.uzabase.playtest2.ModuleKey
import java.net.URL

object HttpModuleKey : ModuleKey
data class HttpModuleConfiguration(val endpoint: URL) : ModuleConfiguration

fun http(endpoint: URL): ConfigurationEntry =
    ConfigurationEntry(HttpModuleKey, HttpModuleConfiguration(endpoint))