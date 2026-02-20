import com.ps.framework.components.id.IdGenerator
import com.ps.personne.personne
import io.kotest.provided.GlobalPostgresContainer
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.TemporalAmount
import java.util.*

object TestUUIDGenerator : IdGenerator {

    private val generatedIds = mutableListOf<UUID>()

    override fun next(): UUID = UUID.randomUUID().also(generatedIds::add)
    operator fun get(index: Int) = generatedIds[index]
}

object FixedTestClockWithFixedIncrement : Clock() {
    private var nextInstant: Instant = Instant.now()
    private var increment: TemporalAmount = Duration.ofMinutes(1)

    fun startInstant(instant: Instant) {
        this.nextInstant = instant
    }

    fun incrementBy(increment: TemporalAmount) {
        this.increment = increment
    }

    override fun instant(): Instant = nextInstant.also { nextInstant = nextInstant.plus(increment) }

    override fun withZone(zone: ZoneId): Clock {
        error("Test clock does not support changing time zones.")
    }

    override fun getZone(): ZoneId = ZoneId.systemDefault()
}

object TestApp {
    private val builder: ApplicationTestBuilder by lazy {
        ApplicationTestBuilder().apply(
            {
                environment {
                    config =
                        MapApplicationConfig(
                            "database.url" to GlobalPostgresContainer.jdbcUrl,
                            "database.user" to GlobalPostgresContainer.username,
                            "database.password" to GlobalPostgresContainer.password,
                            "database.schema" to "personne",
                            "database.driverClassName" to "org.postgresql.Driver",
                            "database.maximumPoolSize" to "5",
                            "database.minimumIdle" to "1",
                            "database.idleTimeout" to "600000",
                            "database.connectionTimeout" to "30000",
                            "database.maxLifetime" to "1800000",
                        )
                }
                application {
                    personne {
                        single<Clock> { FixedTestClockWithFixedIncrement }
                        single<EntreeHistoriqueIdGenerator> { TestUUIDGenerator }
                        single<IdGenerator> { TestUUIDGenerator }
                    }
                }
            },
        )
    }

    const val DEFAULT_TENANT_ID = "pack"

    const val DEFAULT_LOGIN = "john.doe"

    /** basic client with pre-set login and tenantId (from defaultTenantId) headers */
    val defaultClient: HttpClient by lazy {
        httpClient {
            install(ContentNegotiation) { json(Json { prettyPrint = true }) }
            defaultRequest {
                headers.appendIfNameAbsent("login", DEFAULT_LOGIN)
                headers.appendIfNameAbsent("tenantId", DEFAULT_TENANT_ID)
            }
        }
    }

    fun httpClient(block: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit) = builder.createClient(block)
    suspend fun start() = builder.startApplication()
}
