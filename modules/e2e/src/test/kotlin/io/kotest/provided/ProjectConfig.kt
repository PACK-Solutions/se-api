package io.kotest.provided

import com.ps.personne.personne
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.listeners.BeforeProjectListener
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.util.appendIfNameAbsent
import org.testcontainers.postgresql.PostgreSQLContainer

object GlobalPostgresContainer : PostgreSQLContainer(
    "postgres:18",
) {
    init {
        withDatabaseName("test")
        withUsername("user")
        withPassword("password")
    }
}

object KtorTestApp {
    val builder: ApplicationTestBuilder by lazy {
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
    val defaultHttpClient: HttpClient by lazy {
        httpClient {
            install(ContentNegotiation) { json() }
            defaultRequest {
                headers.appendIfNameAbsent("login", "john.doe")
                headers.appendIfNameAbsent("tenantId", defaultTenantId)
            }
        }
    }

    fun httpClient(block: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit) = builder.createClient(block)
}

object KtorTestAppStartup : BeforeProjectListener, AfterProjectListener {
    override suspend fun beforeProject() {
        println("Starting Ktor Test App")
        KtorTestApp.builder.startApplication()
        println("Ktor Test App started ")
    }

    override suspend fun afterProject() {
        println("Stopping Ktor Test App")
    }
}

object TestContainerStartup : BeforeProjectListener, AfterProjectListener, BeforeEachListener {

    override suspend fun beforeProject() {
        if (!GlobalPostgresContainer.isRunning) {
            println("Starting postgres container")
            GlobalPostgresContainer.start()
        }
    }

    override suspend fun afterProject() {
        if (GlobalPostgresContainer.isRunning) {
            println("Stopping postgres container")
            GlobalPostgresContainer.stop()
        }
    }
}

class ProjectConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(TestContainerStartup, KtorTestAppStartup)
}
