package com.ps.personne.rest

import com.ps.personne.assembly.configureConnaissanceClientService
import com.ps.personne.database.config.DatabaseConfig
import com.ps.personne.database.health.HealthCheckService
import com.ps.personne.rest.config.CorsConfig.configureCors
import com.ps.personne.rest.config.SerializationConfig.configureSerialization
import com.ps.personne.rest.config.SwaggerConfig.configureSwagger
import com.ps.personne.rest.connaissance.client.configureConnaissanceClientRoutes
import com.ps.personne.rest.health.configureHealthRoutes
import io.ktor.server.application.Application
import io.ktor.server.config.property

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * Application module configuration
 * Each configuration function is extracted to its own class
 */
fun Application.personne() {
    configureCors()
    configureSwagger()
    configureSerialization()
    property<DatabaseConfig>("database").apply { configureDatabases() }
    configureHealthRoutes(HealthCheckService())
    configureConnaissanceClientRoutes(configureConnaissanceClientService())
}
