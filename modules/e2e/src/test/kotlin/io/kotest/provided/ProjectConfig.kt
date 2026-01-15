package io.kotest.provided

import TestApp
import com.diffplug.selfie.kotest.SelfieExtension
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.listeners.BeforeProjectListener
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

object KtorTestAppStartup : BeforeProjectListener, AfterProjectListener {
    override suspend fun beforeProject() {
        println("Starting Ktor Test App")
        TestApp.start()
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
    override fun extensions() = listOf(TestContainerStartup, KtorTestAppStartup, SelfieExtension(this))
}
