package com.ps.personne.database.model

import com.ps.framework.components.diff.Diff
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ChangementDto {
    abstract val proprieteObjet: String

    @Serializable
    @SerialName("Creation")
    data class Creation(override val proprieteObjet: String, val newValue: String) : ChangementDto()

    @Serializable
    @SerialName("Modification")
    data class Modification(override val proprieteObjet: String, val newValue: String, val oldValue: String) :
        ChangementDto()

    @Serializable
    @SerialName("Suppression")
    data class Suppression(override val proprieteObjet: String, val oldValue: String) : ChangementDto()

    fun toDiff() = when (this) {
        is Creation -> Diff.Create(proprieteObjet, newValue)
        is Modification -> Diff.Update(proprieteObjet, newValue, oldValue)
        is Suppression -> Diff.Delete(proprieteObjet, oldValue)
    }

    companion object {
        fun from(changement: Diff) = when (changement) {
            is Diff.Create -> Creation(changement.proprieteObjet, changement.newValue)

            is Diff.Update -> Modification(
                changement.proprieteObjet,
                changement.newValue,
                changement.oldValue,
            )

            is Diff.Delete -> Suppression(changement.proprieteObjet, changement.oldValue)
        }
    }
}
