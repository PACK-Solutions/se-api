package com.ps.personne

import com.ps.framework.cqrs.bus.ContextKey

sealed class PersonneContextKey<T : Any> : ContextKey<T> {
    object Login : PersonneContextKey<String>()
    object TenantId : PersonneContextKey<String>()
}
