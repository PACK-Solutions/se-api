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
                application { personne() }
            },
        )
    }

    const val defaultTenantId = "pack"

    /** basic client with pre-set login and tenantId (from defaultTenantId) headers */
    val defaultClient: HttpClient by lazy {
        httpClient {
            install(ContentNegotiation) { json(Json { prettyPrint = true }) }
            defaultRequest {
                headers.appendIfNameAbsent("login", "john.doe")
                headers.appendIfNameAbsent("tenantId", defaultTenantId)
            }
        }
    }

    fun httpClient(block: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit) = builder.createClient(block)
    suspend fun start() = builder.startApplication()
}
