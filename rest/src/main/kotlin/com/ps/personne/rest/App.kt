package com.ps.personne.rest

import com.ps.personne.rest.config.CorsConfig.configureCors
import com.ps.personne.rest.config.DatabaseConfig.Companion.configureDatabases
import com.ps.personne.rest.config.SerializationConfig.configureSerialization
import com.ps.personne.rest.config.SwaggerConfig.configureSwagger
import com.ps.personne.rest.exposition_politique.configureExpositionPolitiqueRoutes
import com.ps.personne.rest.health.HealthCheckService
import com.ps.personne.rest.health.configureHealthRoutes
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * Application module configuration
 * Each configuration function is extracted to its own class
 */
fun Application.personne() {
    configureSwagger()
    configureCors()
    //configureDatabases()
    configureSerialization()
    configureHealthRoutes(HealthCheckService())
    configureExpositionPolitiqueRoutes()

    routing {
        get("/") {
            call.respond("Hello World!")
        }
    }
}
