package com.ps.personne.rest.dto.response

import com.ps.personne.historique.Diff
import com.ps.personne.historique.EntreeHistorique
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EntreeHistoriqueDto(
    val idObjet: String,
    val changements: Set<ChangementDto>,
    val performedBy: String,
    val occurredAt: String,
)

@Serializable
sealed class ChangementDto {
    abstract val proprieteObjet: String

    @Serializable
    @SerialName("Creation")
    data class Creation(override val proprieteObjet: String, val newValue: String) : ChangementDto()

    @Serializable
    @SerialName("Modification")
    data class Modification(override val proprieteObjet: String, val newValue: String, val oldValue: String) : ChangementDto()

    @Serializable
    @SerialName("Suppression")
    data class Suppression(override val proprieteObjet: String, val oldValue: String) : ChangementDto()

    companion object {
        fun from(changement: Diff) = when (changement) {
            is Diff.Creation -> Creation(changement.proprieteObjet, changement.newValue)
            is Diff.Modification -> Modification(
                changement.proprieteObjet,
                changement.newValue,
                changement.oldValue,
            )

            is Diff.Suppression -> Suppression(changement.proprieteObjet, changement.oldValue)
        }
    }
}

fun EntreeHistorique.toDto() = EntreeHistoriqueDto(
    idObjet = this.idObjet.value,
    changements = this.changements.map(ChangementDto::from).toSet(),
    performedBy = this.performedBy.value,
    occurredAt = this.occurredAt.toString(),
)
