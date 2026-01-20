package com.ps.personne.historique

import java.time.Instant
import java.util.*

@JvmInline
value class TypeObjet(val value: String)

@JvmInline
value class IdObjet(val value: String)

@JvmInline
value class Author(val value: String)

data class EntreeHistorique(
    val id: UUID,
    val typeObjet: TypeObjet,
    val idObjet: IdObjet,
    val changements: Set<Diff> = emptySet(),
    val performedBy: Author,
    val occurredAt: Instant,
)
