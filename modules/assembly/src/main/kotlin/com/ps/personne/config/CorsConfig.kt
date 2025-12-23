package com.ps.personne.config

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

object CorsConfig {
    fun Application.configureCors() {
        install(CORS) {
            anyMethod()
            // Allow commonly used request headers
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.Accept)
            allowHeader(HttpHeaders.AcceptLanguage)
            allowHeader(HttpHeaders.AcceptEncoding)
            allowHeader(HttpHeaders.Origin)
            // Custom app header used by routes
            allowHeader("login")
            allowHeader("tenantId")

            // Permit non-simple content types like application/json
            allowNonSimpleContentTypes = true

            // Explicitly allow the frontend origin
            allowHost("*")
        }
    }
}
