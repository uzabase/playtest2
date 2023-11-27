import com.example.demoapp.App
import com.github.tomakehurst.wiremock.WireMockServer
import com.thoughtworks.gauge.BeforeSuite
import com.thoughtworks.gauge.Step

class Steps {
    private val innerApi = WireMockServer(3000)

    @BeforeSuite
    fun beforeSuite() {
        innerApi.start()
    }
}