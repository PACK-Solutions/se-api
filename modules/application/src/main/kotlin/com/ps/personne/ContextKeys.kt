package com.ps.personne

import com.ps.kommand.ContextKey

sealed class PersonneContextKey<T : Any> : ContextKey<T> {
    object Login : PersonneContextKey<String>()
}
