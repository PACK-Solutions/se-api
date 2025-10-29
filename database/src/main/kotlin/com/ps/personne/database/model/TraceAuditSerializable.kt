package com.ps.personne.database.model

import kotlinx.serialization.Serializable

@Serializable
enum class TypeOperationSer {
    MODIFICATION,
    CORRECTION
}

@Serializable
data class TraceAuditSer(val date: String, val user: String, val typeOperation: TypeOperationSer)
