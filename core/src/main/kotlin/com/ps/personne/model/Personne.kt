package com.ps.personne.model

import java.util.UUID

data class Personne(
    val idPersonne: IdPersonne,
    val expositionPolitique: ExpositionPolitique,
)

@JvmInline
value class IdPersonne(val id: UUID)
