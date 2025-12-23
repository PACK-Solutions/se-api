package com.ps.personne.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.path
import org.slf4j.event.Level

object LoggingConfig {
    fun Application.configureLogging() {
        install(CallLogging) {
            level = Level.INFO
            // Disable call logging for health endpoints explicitly
            filter { call ->
                val path = call.request.path()
                val isHealth = path.contains("/health")
                !isHealth
            }
        }
    }
}
