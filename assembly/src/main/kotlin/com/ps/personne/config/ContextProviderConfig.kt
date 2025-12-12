package com.ps.personne.config

import com.ps.kommand.Context
import com.ps.kommand.withCommandContext
import com.ps.personne.http.HeaderNames
import com.ps.personne.repository.CoroutineContextTenantIdProvider
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
