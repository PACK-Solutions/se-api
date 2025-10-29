package com.ps.personne.database.mapper

import com.ps.personne.database.model.TraceAuditSer
import com.ps.personne.database.model.TypeOperationSer
import com.ps.personne.model.TraceAudit
import com.ps.personne.model.TypeOperation
import com.ps.personne.model.User
import java.time.Instant

private fun Instant.toIsoString(): String = this.toString()

fun TraceAudit.toSer(): TraceAuditSer = TraceAuditSer(
    date = this.date.toIsoString(),
    user = this.user.login,
    typeOperation = TypeOperationSer.valueOf(this.typeOperation.name)
)

fun TraceAuditSer.toDomain(): TraceAudit = TraceAudit(
    user = User(login = this.user),
    date = Instant.parse(date),
    typeOperation = TypeOperation.valueOf(this.typeOperation.name)
)
