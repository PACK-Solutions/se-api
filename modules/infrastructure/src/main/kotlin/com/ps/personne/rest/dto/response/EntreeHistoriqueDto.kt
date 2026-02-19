package com.ps.personne.rest.dto.response

import com.ps.framework.components.diff.Diff
import com.ps.framework.components.history.HistoryEvent
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

fun HistoryEvent.toDto() = EntreeHistoriqueDto(
    idObjet = this.idObjet.value,
    changements = this.diffs.map(ChangementDto::from).toSet(),
    performedBy = this.performedBy.value,
    occurredAt = this.occurredAt.toString(),
)
