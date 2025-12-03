package com.ps.personne

import com.ps.personne.config.CorsConfig.configureCors
import com.ps.personne.config.DatabaseConfig
import com.ps.personne.config.ExceptionHandlingConfig.configureExceptionHandling
import com.ps.personne.config.LoggingConfig.configureLogging
import com.ps.personne.config.SerializationConfig.configureSerialization
import com.ps.personne.config.SwaggerConfig.configureSwagger
import com.ps.personne.config.configureConnaissanceClientService
import com.ps.personne.connaissance.client.configureConnaissanceClientRoutes
import com.ps.personne.health.HealthCheckService
import com.ps.personne.health.configureHealthRoutes
import com.ps.personne.http.MandatoryHeadersPlugin
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
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
    install(MandatoryHeadersPlugin)
    val sandbox = isSandbox()
    if (sandbox) {
        log.info("Sandbox mode enabled: skipping database configuration and migrations")
    } else {
        property<DatabaseConfig>("database").apply { configureDatabases() }
    }
    configureHealthRoutes(HealthCheckService(sandbox))
    configureLogging()
    configureConnaissanceClientRoutes(configureConnaissanceClientService(sandbox))
}

private fun Application.isSandbox(): Boolean =
    environment.config.propertyOrNull("environment.sandbox")?.getString()?.toBoolean() ?: false
