package com.ps.personne

import com.ps.personne.config.CorsConfig.configureCors
import com.ps.personne.config.DatabaseConfig
import com.ps.personne.config.ExceptionHandlingConfig.configureExceptionHandling
import com.ps.personne.config.SerializationConfig.configureSerialization
import com.ps.personne.config.SwaggerConfig.configureSwagger
import com.ps.personne.config.configureConnaissanceClientService
import com.ps.personne.connaissance.client.configureConnaissanceClientRoutes
import com.ps.personne.health.HealthCheckService
import com.ps.personne.health.configureHealthRoutes
import io.ktor.server.application.Application
import io.ktor.server.config.property
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.personne() {
    configureCors()
    configureSwagger()
    configureSerialization()
    configureExceptionHandling()
    property<DatabaseConfig>("database").apply { configureDatabases() }
    configureHealthRoutes(HealthCheckService())
    configureConnaissanceClientRoutes(configureConnaissanceClientService())
}
