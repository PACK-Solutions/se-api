package com.ps.personne.mapper

import com.ps.personne.model.*
import java.time.Instant

private fun Instant.toIsoString(): String = this.toString()

fun TraceAudit.toSer(): TraceAuditSer = TraceAuditSer(
    date = this.date.toIsoString(),
    user = this.user.login,
    typeOperation = TypeOperationSer.valueOf(this.typeOperation.name),
)

fun TraceAuditSer.toDomain(): TraceAudit = TraceAudit(
    user = User(login = this.user),
    date = Instant.parse(date),
    typeOperation = TypeOperation.valueOf(this.typeOperation.name),
)
