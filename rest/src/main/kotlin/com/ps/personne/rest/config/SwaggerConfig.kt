package com.ps.personne.rest.config

import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

/**
 * Configuration for Swagger UI
 */
object SwaggerConfig {
    /**
     * Configure Swagger UI for the application
     */
    fun Application.configureSwagger() {
        val swaggerPath = "swagger"

        routing {
            swaggerUI(path = swaggerPath, swaggerFile = "openapi/documentation.yaml")
        }

        environment.config.propertyOrNull("ktor.deployment.port")?.let {
            // Display the Swagger URL at application startup
            log.info("📚 Swagger UI available at: http://localhost:$it/$swaggerPath")
        } ?: {
            val message = "No application port configured, check the property 'ktor.deployment.port'"
            log.error(message)
            throw IllegalStateException(message)
        }
    }
}
