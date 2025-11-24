package com.ps.personne.kyc.dto.response

import com.ps.personne.model.*
import kotlinx.serialization.Serializable

@Serializable
data class HistoriqueModificationDto(
    val idPersonne: Long,
    val entreesHistorique: List<SyntheseModificationDto>,
)

@Serializable
data class SyntheseModificationDto(
    val traceAudit: TraceAuditDto,
    val modifications: Set<ModificationDto>,
)

@Serializable
data class ModificationDto(
    val modification: String,
    val motifsVigilance: Set<String> = emptySet(),
)

@Serializable
data class TraceAuditDto(
    val date: String,
    val user: String,
    val typeOperation: String,
)

fun HistoriqueModifications.toDto(): HistoriqueModificationDto = HistoriqueModificationDto(
    idPersonne = this.idPersonne.id,
    entreesHistorique = this.entreesHistorique.map { synthese ->
        SyntheseModificationDto(
            traceAudit = TraceAuditDto(
                date = synthese.traceAudit.date.toString(),
                user = synthese.traceAudit.user.login,
                typeOperation = synthese.traceAudit.typeOperation.name,
            ),
            modifications = synthese.modifications.map { it.toDto() }.toSet(),
        )
    },
)

private fun ModificationConnaissanceClient.toDto(): ModificationDto = when (this) {
    is AjoutStatutPPE -> ModificationDto("AjoutStatutPPE")
    is SuppressionStatutPPE -> ModificationDto("SuppressionStatutPPE")
    is ModificationFonctionPPE -> ModificationDto("ModificationFonctionPPE")
    is ModificationDateFinFonctionPPE -> ModificationDto("ModificationDateFinFonctionPPE")
    is AjoutStatutProchePPE -> ModificationDto("AjoutStatutProchePPE")
    is SuppressionStatutProchePPE -> ModificationDto("SuppressionStatutProchePPE")
    is ModificationLienParente -> ModificationDto("ModificationLienParente")
    is ModificationFonctionProchePPE -> ModificationDto("ModificationFonctionProchePPE")
    is AjoutVigilance -> ModificationDto("AjoutVigilance")
    is SuppressionVigilance -> ModificationDto("SuppressionVigilance")
    is AjoutMotifVigilance -> ModificationDto("AjoutMotifVigilance", this.motifs.map { it.name }.toSet())
    is SuppressionMotifVigilance -> ModificationDto("SuppressionMotifVigilance")
    is ModificationMotifVigilance -> ModificationDto("ModificationMotifVigilance", this.motifs.map { it.name }.toSet())
}
