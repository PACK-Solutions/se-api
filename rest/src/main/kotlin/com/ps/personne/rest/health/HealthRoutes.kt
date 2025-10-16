package com.ps.personne.rest.health

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.routing

/**
 * Configure health check routes
 */
@Suppress("TooGenericExceptionCaught")
fun Application.configureHealthRoutes(healthCheckService: HealthCheckService) {
    routing {
        // GET for humans and monitoring systems expecting a JSON body
        get("/health") {
            try {
                val healthResult = healthCheckService.performHealthCheck()

                val statusCode = if (healthResult.status.name == "UP") {
                    HttpStatusCode.OK
                } else {
                    HttpStatusCode.ServiceUnavailable
                }

                call.respond(statusCode, healthResult)
            } catch (e: Exception) {
                log.error("Health check failed with exception", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf(
                        "status" to "DOWN",
                        "error" to (e.message ?: "Unknown error"),
                    ),
                )
            }
        }

        // HEAD for Docker/`wget --spider` health checks (no body expected)
        head("/health") {
            try {
                val healthResult = healthCheckService.performHealthCheck()
                val statusCode = if (healthResult.status.name == "UP") {
                    HttpStatusCode.OK
                } else {
                    HttpStatusCode.ServiceUnavailable
                }
                // Respond with status only; HEAD ignores the body per RFC
                call.respond(statusCode)
            } catch (e: Exception) {
                log.error("Health check failed with exception", e)
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
