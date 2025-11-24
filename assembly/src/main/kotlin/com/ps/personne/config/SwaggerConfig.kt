package com.ps.personne.config

import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

object SwaggerConfig {
    fun Application.configureSwagger() {
        val swaggerPath = "swagger"

        routing {
            swaggerUI(path = swaggerPath, swaggerFile = "openapi/documentation.yaml")
        }

        environment.config.propertyOrNull("ktor.deployment.port")?.let {
            log.info("📚 Swagger UI available at: http://localhost:$it/$swaggerPath")
        } ?: {
            val message = "No application port configured, check the property 'ktor.deployment.port'"
            log.error(message)
            throw IllegalStateException(message)
        }
    }
}
