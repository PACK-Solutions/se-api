package com.ps.personne.config

import com.ps.kommand.Context
import com.ps.kommand.withCommandContext
import com.ps.personne.database.repository.CoroutineContextTenantIdProvider
import com.ps.personne.rest.config.HeaderNames
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call

object ContextProviderConfig {
    fun Application.configureContextProvider() {
        this.intercept(ApplicationCallPipeline.Setup) {
            val tenantId = call.request.headers[HeaderNames.TENANT_ID] ?: error("No tenant")
            val context = Context()
            context[CoroutineContextTenantIdProvider.Companion.TenantIdKey] = tenantId
            withCommandContext(context) {
                proceed()
            }
        }
    }
}
