import com.thoughtworks.gauge.AfterSuite
import com.thoughtworks.gauge.BeforeSuite
import com.uzabase.playtest2.core.config.Configuration
import com.uzabase.playtest2.jdbc.config.jdbc
import org.testcontainers.containers.PostgreSQLContainer
import java.util.logging.Level
import java.util.logging.LogManager

class Sample {
    private val container = PostgreSQLContainer<Nothing>("postgres:17.0-bookworm").apply {
        withDatabaseName("testdatabase")
        withUsername("michel")
        withPassword("goodpassphrase")
        withInitScript("setup.sql")
    }

    @BeforeSuite
    fun beforeSuite() {
        LogManager.getLogManager().getLogger("").level = Level.OFF
        container.start()

        Configuration.playtest2 {
            listOf(
                jdbc("test", container.jdbcUrl, container.username, container.password)
            )
        }
    }

    @AfterSuite
    fun afterSuite() {
        container.stop()
    }
}