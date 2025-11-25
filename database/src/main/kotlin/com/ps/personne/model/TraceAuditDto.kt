package com.ps.personne.model

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
enum class TypeOperationDto {
    MODIFICATION,
    CORRECTION
}

@Serializable
data class TraceAuditDto(val date: String, val user: String, val typeOperation: TypeOperationDto)

private fun Instant.toIsoString(): String = this.toString()

fun TraceAudit.toDto(): TraceAuditDto = TraceAuditDto(
    date = this.date.toIsoString(),
    user = this.user.login,
    typeOperation = TypeOperationDto.valueOf(this.typeOperation.name),
)

fun TraceAuditDto.toDomain(): TraceAudit = TraceAudit(
    user = User(login = this.user),
    date = Instant.parse(date),
    typeOperation = TypeOperation.valueOf(this.typeOperation.name),
)
