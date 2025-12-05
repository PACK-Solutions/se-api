package com.ps.personne.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

private val logger = KotlinLogging.logger {}

@Serializable
data class DatabaseConfig(
    val url: String,
    val user: String,
    val password: String,
    val driverClassName: String,
    val maximumPoolSize: Int,
    val minimumIdle: Int,
    val idleTimeout: Long,
    val connectionTimeout: Long,
    val maxLifetime: Long,
) {

    fun configureDatabases() {
        val hikari = HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = this@DatabaseConfig.url
                username = this@DatabaseConfig.user
                password = this@DatabaseConfig.password
                driverClassName = this@DatabaseConfig.driverClassName
                maximumPoolSize = this@DatabaseConfig.maximumPoolSize
                minimumIdle = this@DatabaseConfig.minimumIdle
                idleTimeout = this@DatabaseConfig.idleTimeout
                connectionTimeout = this@DatabaseConfig.connectionTimeout
                maxLifetime = this@DatabaseConfig.maxLifetime
            },
        )

        Database.connect(datasource = hikari)

        logger.info { "\uD83D\uDDC4\uFE0F Connection to database: OK" }

        val baselineOnMigrate = System.getenv("FLYWAY_BASELINE_ON_MIGRATE")?.toBoolean() ?: false

        val flyway =
            Flyway
                .configure()
                .dataSource(hikari)
                .baselineOnMigrate(baselineOnMigrate)
                .locations("classpath:db/migration")
                .skipDefaultCallbacks(true)
                .load()

        transaction {
            flyway.migrate()
        }
    }
}
