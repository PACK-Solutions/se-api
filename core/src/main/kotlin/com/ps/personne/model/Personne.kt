package com.ps.personne.model

import java.util.UUID

data class Personne(
    val idPersonne: IdPersonne,
    val expositionPolitique: ExpositionPolitique,
) {
    fun ajouterExpositionPolitique(expositionPolitique: ExpositionPolitique, traceAudit: TraceAudit) {
        TODO("Not yet implemented")
    }
}

@JvmInline
value class IdPersonne(val id: UUID)
