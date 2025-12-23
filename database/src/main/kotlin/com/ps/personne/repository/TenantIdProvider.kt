package com.ps.personne.repository

import com.ps.kommand.ContextKey
import com.ps.kommand.ContextProvider

interface TenantIdProvider {
    fun tenantId(): String
}

class CoroutineContextTenantIdProvider : TenantIdProvider {
    override fun tenantId(): String = ContextProvider.Coroutine.current()[TenantIdKey] ?: error("current TenantId not found in context")

    companion object {
        object TenantIdKey : ContextKey<String>
    }
}
