import com.example.demoapp.App
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.thoughtworks.gauge.AfterSuite
import com.thoughtworks.gauge.BeforeScenario
import com.thoughtworks.gauge.BeforeSuite
import com.uzabase.playtest2.core.config.Configuration.Companion.playtest2
import com.uzabase.playtest2.core.config.plus
import com.uzabase.playtest2.http.config.http
import com.uzabase.playtest2.wiremock.config.wireMock
import java.net.URI

class Steps {
    private val innerApi = WireMockServer(3000)

    @BeforeSuite
    fun beforeSuite() {
        playtest2 {
            http(URI("http://localhost:8080").toURL()) +
                    wireMock("InnerAPI", URI("http://0.0.0.0:3000").toURL())
        }
        App.startServer()
        innerApi.start()
    }

    @BeforeScenario
    fun resetMock() {
        WireMock(3000).resetRequests()
    }


    @AfterSuite
    fun afterSuite() {
        innerApi.stop()
        App.stopServer()
    }
}