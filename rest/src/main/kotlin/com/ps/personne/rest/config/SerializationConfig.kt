package com.ps.personne.rest.config

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

object SerializationConfig {
    /**
     * Configure JSON serialization for the application
     */
    fun Application.configureSerialization() {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    coerceInputValues = true
                },
            )
        }
    }
}
