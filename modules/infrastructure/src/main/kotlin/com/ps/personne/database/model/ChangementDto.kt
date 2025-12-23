package com.ps.personne.database.model

import com.ps.personne.historique.Changement
import kotlinx.serialization.Serializable

@Serializable
sealed class ChangementDto {
    abstract val proprieteObjet: String

    @Serializable
    data class Creation(override val proprieteObjet: String, val newValue: String) : ChangementDto()

    @Serializable
    data class Modification(override val proprieteObjet: String, val newValue: String, val oldValue: String) : ChangementDto()

    @Serializable
    data class Suppression(override val proprieteObjet: String, val oldValue: String) : ChangementDto()

    companion object {
        fun from(changement: Changement) = when (changement) {
            is Changement.Creation -> Creation(changement.proprieteObjet, changement.newValue)
            is Changement.Modification -> Modification(
                changement.proprieteObjet,
                changement.newValue,
                changement.oldValue
            )
            is Changement.Suppression -> Suppression(changement.proprieteObjet, changement.oldValue)
        }
    }
}
