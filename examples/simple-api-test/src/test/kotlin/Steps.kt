import com.example.demoapp.App
import com.github.tomakehurst.wiremock.WireMockServer
import com.thoughtworks.gauge.AfterSuite
import com.thoughtworks.gauge.BeforeSuite
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.http.config.http
import com.uzabase.playtest2.wiremock.config.wireMock
import com.uzabase.playtest2.core.config.plus
import java.net.URI

class Steps {
    private val innerApi = WireMockServer(3000)

    @BeforeSuite
    fun beforeSuite() {
        Configuration.playtest2 {
            http(URI("http://localhost:8080").toURL()) +
                    wireMock("InnerAPI", URI("http://localhost:3000").toURL())
        }
        App.startServer()
        innerApi.start()
    }

    @AfterSuite
    fun afterSuite() {
        innerApi.stop()
        App.stopServer()
    }
}