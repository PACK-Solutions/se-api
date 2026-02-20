package com.ps.personne.database.model

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
enum class TypeOperationDto {
    MODIFICATION,
    CORRECTION,
}

@Serializable
data class TraceAuditDto(val date: String, val user: String, val typeOperation: TypeOperationDto)

private fun Instant.toIsoString(): String = this.toString()

fun com.ps.personne.model.TraceAudit.toDto(): TraceAuditDto = TraceAuditDto(
    date = this.date.toIsoString(),
    user = this.user.login,
    typeOperation = TypeOperationDto.valueOf(this.typeOperation.name),
)

fun TraceAuditDto.toDomain(): com.ps.personne.model.TraceAudit = _root_ide_package_.com.ps.personne.model.TraceAudit(
    user = _root_ide_package_.com.ps.personne.model.User(login = this.user),
    date = Instant.parse(date),
    typeOperation = _root_ide_package_.com.ps.personne.model.TypeOperation.valueOf(this.typeOperation.name),
)
