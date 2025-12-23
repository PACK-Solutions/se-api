package com.ps.personne.fixtures

import com.ps.personne.model.AjoutVigilance
import com.ps.personne.model.SyntheseModifications

object ModificationConnaissanceClientFactory {
    fun creerSyntheseModifications() = SyntheseModifications(
        traceAudit = TraceAuditFactory.creerTraceAuditModification(),
        modifications = setOf(AjoutVigilance),
    )
}
