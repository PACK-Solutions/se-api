package com.ps.personne.rest.config

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

/**
 * Configuration for CORS (Cross-Origin Resource Sharing)
 */
object CorsConfig {
    /**
     * Configure CORS for the application
     */
    fun Application.configureCors() {
        install(CORS) {
            // Methods commonly used by the frontend (preflight + CRUD)
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Patch)
            allowMethod(HttpMethod.Delete)

            // Allow commonly used request headers
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.Accept)
            allowHeader(HttpHeaders.AcceptLanguage)
            allowHeader(HttpHeaders.AcceptEncoding)
            allowHeader(HttpHeaders.Origin)
            allowHeader("X-Requested-With")
            // Custom app header used by routes
            allowHeader("login")

            // Permit non-simple content types like application/json
            allowNonSimpleContentTypes = true

            // Explicitly allow the frontend origin
            allowHost("*")
        }
    }
}
