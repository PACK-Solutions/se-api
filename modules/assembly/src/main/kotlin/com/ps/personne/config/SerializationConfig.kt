package com.ps.personne.config

import com.ps.framework.ktor.configuration.psDefaults
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

object SerializationConfig {
    fun Application.configureSerialization() {
        install(ContentNegotiation) {
            psDefaults()
        }
    }
}
