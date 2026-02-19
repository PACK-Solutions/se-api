package com.ps.personne.testharness.fixtures

import com.ps.framework.cqrs.bus.Context
import com.ps.personne.PersonneContextKey

class ContextFixture {
    private val context: Context = Context()

    fun withLogin(login: String) = this.apply { context[PersonneContextKey.Login] = login }

    fun build() = context
}

fun aContext() = ContextFixture()
