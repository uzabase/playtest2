package com.uzabase.playtest2.example

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.thoughtworks.gauge.AfterScenario
import com.thoughtworks.gauge.AfterSuite
import com.thoughtworks.gauge.BeforeSuite
import com.thoughtworks.gauge.Step
import com.uzabase.playtest2.core.config.Configuration.Companion.playtest2
import com.uzabase.playtest2.core.config.plus
import com.uzabase.playtest2.http.config.http
import com.uzabase.playtest2.wiremock.config.wireMock
import java.net.URI

class CompatibleSteps {
    private val proxyServer =
        WireMockConfiguration().port(3000)
            .usingFilesUnderClasspath("proxy_server")
            .let(::WireMockServer)

    private val proxiedApi =
        WireMockConfiguration().port(3010)
            .usingFilesUnderClasspath("proxied_api")
            .let(::WireMockServer)


    @BeforeSuite
    fun beforeSuite() {
        playtest2 {
            http(URI("http://localhost:3000").toURL()) +
                    wireMock("ProxiedAPI", URI("http://localhost:3010").toURL())
        }
        proxiedApi.start()
        proxyServer.start()
    }

    @AfterScenario
    fun afterScenario() {
        WireMock(3000).resetRequests()
        WireMock(3010).resetRequests()
    }

    @AfterSuite
    fun afterSuite() {
        proxiedApi.stop()
        proxyServer.stop()
    }

    @Step("Hello")
    fun hello() {
        Thread.sleep(5000)
    }
}