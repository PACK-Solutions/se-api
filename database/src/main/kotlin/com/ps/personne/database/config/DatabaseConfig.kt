package com.ps.personne.database.config

import com.ps.personne.database.tables.ConnaissanceClientTable
import com.ps.personne.database.tables.HistoriqueModificationConnaissanceClientTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
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

        val database = Database.connect(datasource = hikari)

        logger.info { "\uD83D\uDDC4\uFE0F Connection to database: OK" }

        val flyway =
            Flyway
                .configure()
                .dataSource(hikari)
                .baselineOnMigrate(true) // Used when migrating an existing database for the first time
                .load()

        transaction(database) {
            flyway.migrate()
        }

        // Create tables if they don't exist
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(ConnaissanceClientTable, HistoriqueModificationConnaissanceClientTable)
        }
    }
}
