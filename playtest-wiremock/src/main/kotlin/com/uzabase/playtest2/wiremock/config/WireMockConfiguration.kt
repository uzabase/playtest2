package com.uzabase.playtest2.wiremock.config

import com.uzabase.playtest2.core.config.ConfigurationEntry
import com.uzabase.playtest2.core.config.ModuleConfiguration
import com.uzabase.playtest2.core.config.ModuleKey
import java.net.URL

internal data class WireMockModuleKey(val name: String) : ModuleKey
internal data class WireMockModuleConfiguration(val endpoint: URL) : ModuleConfiguration

fun wireMock(name: String, endpoint: URL): ConfigurationEntry =
    ConfigurationEntry(WireMockModuleKey(name), WireMockModuleConfiguration(endpoint))