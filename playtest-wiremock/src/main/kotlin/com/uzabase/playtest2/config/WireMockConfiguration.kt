package com.uzabase.playtest2.config

import com.uzabase.playtest2.ConfigurationEntry
import com.uzabase.playtest2.ModuleConfiguration
import com.uzabase.playtest2.ModuleKey
import java.net.URL

data class WireMockModuleKey(val name: String) : ModuleKey
data class WireMockModuleConfiguration(val endpoint: URL) : ModuleConfiguration

fun wireMock(name: String, endpoint: URL): ConfigurationEntry =
    ConfigurationEntry(WireMockModuleKey(name), WireMockModuleConfiguration(endpoint))