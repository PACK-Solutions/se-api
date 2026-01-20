package com.ps.personne

import com.ps.kommand.CommandBus
import com.ps.kommand.QueryBus
import com.ps.personne.config.ContextProviderConfig.configureContextProvider
import com.ps.personne.config.CorsConfig.configureCors
import com.ps.personne.config.ExceptionHandlingConfig.configureExceptionHandling
import com.ps.personne.config.InstancesConfig.configureKoin
import com.ps.personne.config.LoggingConfig.configureLogging
import com.ps.personne.config.SerializationConfig.configureSerialization
import com.ps.personne.config.SwaggerConfig.configureSwagger
import com.ps.personne.database.config.DatabaseConfig
import com.ps.personne.database.health.HealthCheckService
import com.ps.personne.rest.config.MandatoryHeadersPlugin
import com.ps.personne.rest.routes.configureConnaissanceClientRoutes
import com.ps.personne.rest.routes.configureHealthRoutes
import com.ps.personne.rest.routes.configureHistoriqueRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.config.property
import io.ktor.server.netty.EngineMain
import org.koin.core.module.Module
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.personne(moduleOverrides: Module.() -> Unit = {}) {
    configureCors()
    configureSwagger()
    configureSerialization()
    configureExceptionHandling()
    configureContextProvider()
    install(MandatoryHeadersPlugin)
    install(Koin)

    val sandbox = isSandbox()
    if (sandbox) {
        log.info("Sandbox mode enabled: skipping database configuration and migrations")
    } else {
        property<DatabaseConfig>("database").apply { configureDatabases() }
    }
    configureHealthRoutes(HealthCheckService(sandbox))
    configureLogging()

    configureKoin(moduleOverrides)

    configureConnaissanceClientRoutes(get<QueryBus>(), get<CommandBus>())
    configureHistoriqueRoutes(get<QueryBus>())
}

private fun Application.isSandbox(): Boolean = // todo utiliser Application.developmentMode ?
    environment.config.propertyOrNull("environment.sandbox")?.getString()?.toBoolean() ?: false
