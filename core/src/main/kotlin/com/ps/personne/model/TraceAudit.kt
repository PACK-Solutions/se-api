package com.ps.personne.model

import java.time.Instant

data class TraceAudit(
    val user: User,
    val date: Instant,
    var typeOperation: TypeOperation,
)

@JvmInline
value class User(val login: String)

enum class TypeOperation {
    MODIFICATION,
    CORRECTION
}
