package com.ps.personne.rest.config

import com.ps.personne.rest.persistence.ExpositionPolitiqueTable
import com.ps.personne.rest.persistence.PersonTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.config.property
import kotlinx.serialization.Serializable
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

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

    companion object {
        /**
         * Configure databases for the application
         */
        @Suppress("MagicNumber")
        fun Application.configureDatabases() {
            val databaseConfig: DatabaseConfig = property<DatabaseConfig>("database")

            val hikari =
                HikariDataSource(
                    HikariConfig().apply {
                        jdbcUrl = databaseConfig.url
                        username = databaseConfig.user
                        password = databaseConfig.password
                        driverClassName = databaseConfig.driverClassName
                        maximumPoolSize = databaseConfig.maximumPoolSize
                        minimumIdle = databaseConfig.minimumIdle
                        idleTimeout = databaseConfig.idleTimeout
                        connectionTimeout = databaseConfig.connectionTimeout
                        maxLifetime = databaseConfig.maxLifetime
                    },
                )

            val database = Database.connect(datasource = hikari)

            log.info("\uD83D\uDDC4\uFE0F Connection to database: OK")

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
                SchemaUtils.create(PersonTable, ExpositionPolitiqueTable)
            }
        }
    }
}
