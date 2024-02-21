package com.uzabase.playtest2.http.config

import com.uzabase.playtest2.core.assertion.AssertableProxyFactory
import com.uzabase.playtest2.core.config.ConfigurationEntry
import com.uzabase.playtest2.core.config.ModuleConfiguration
import com.uzabase.playtest2.core.config.ModuleKey
import com.uzabase.playtest2.http.assertion.FromHeaders
import java.net.URL

internal object HttpModuleKey : ModuleKey
internal data class HttpModuleConfiguration(val endpoint: URL) : ModuleConfiguration

fun http(
    endpoint: URL,
    assertableProxies: List<AssertableProxyFactory> = listOf(FromHeaders)
): ConfigurationEntry =
    ConfigurationEntry(
        HttpModuleKey,
        HttpModuleConfiguration(endpoint),
        assertableProxies = assertableProxies
    )