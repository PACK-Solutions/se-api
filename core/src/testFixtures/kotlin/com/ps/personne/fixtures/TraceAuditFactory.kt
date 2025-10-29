package com.ps.personne.fixtures

import com.ps.personne.model.TraceAudit
import com.ps.personne.model.TypeOperation
import com.ps.personne.model.User
import java.time.Instant

object TraceAuditFactory {
    fun creerTraceAuditModification() = TraceAudit(
        user = User("test"),
        date = Instant.now(),
        typeOperation = TypeOperation.MODIFICATION,
    )
}
