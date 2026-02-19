package com.ps.personne.config

import com.ps.framework.cqrs.bus.Context
import com.ps.framework.cqrs.bus.withCommandContext
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline

object ContextProviderConfig {
    fun Application.configureContextProvider() {
        intercept(ApplicationCallPipeline.Setup) {
            val context = Context()
            withCommandContext(context) {
                proceed()
            }
        }
    }
}
