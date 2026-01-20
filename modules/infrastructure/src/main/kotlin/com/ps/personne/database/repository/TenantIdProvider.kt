package com.ps.personne.database.repository

import com.ps.kommand.ContextProvider
import com.ps.personne.PersonneContextKey

interface TenantIdProvider {
    fun tenantId(): String
}

class CoroutineContextTenantIdProvider : TenantIdProvider {
    override fun tenantId(): String = ContextProvider.Coroutine.current()[PersonneContextKey.TenantId] ?: error("current TenantId not found in context")
}
