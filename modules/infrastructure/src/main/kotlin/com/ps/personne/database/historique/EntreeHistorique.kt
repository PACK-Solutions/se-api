package com.ps.personne.database.historique

import kotlinx.serialization.Serializable
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

@Serializable
sealed class Changement {
    abstract val proprieteObjet: String

    @Serializable
    data class Creation(override val proprieteObjet: String, val newValue: String) : Changement()

    @Serializable
    data class Modification(override val proprieteObjet: String, val newValue: String, val oldValue: String) : Changement()

    @Serializable
    data class Suppression(override val proprieteObjet: String, val oldValue: String) : Changement()
}

