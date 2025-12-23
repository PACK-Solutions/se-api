package com.ps.personne.historique

import java.time.Instant
import java.util.*

data class EntreeHistorique(
    val id: UUID,
    val typeObjet: String,
    val idObjet: String,
    val changements: Set<Changement> = emptySet(),
    val performedBy: String,
    val occurredAt: Instant,
)

sealed class Changement {
    abstract val proprieteObjet: String

    data class Creation(override val proprieteObjet: String, val newValue: String) : Changement()
    data class Modification(override val proprieteObjet: String, val newValue: String, val oldValue: String) : Changement()
    data class Suppression(override val proprieteObjet: String, val oldValue: String) : Changement()
}
