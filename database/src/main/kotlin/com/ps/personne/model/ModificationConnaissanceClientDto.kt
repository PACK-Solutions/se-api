package com.ps.personne.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ModificationConnaissanceClientDto

@Serializable
@SerialName("ajout_statut_ppe")
object AjoutStatutPPEDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("suppression_statut_ppe")
object SuppressionStatutPPEDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("modification_fonction_ppe")
object ModificationFonctionPPEDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("modification_date_fin_fonction_ppe")
object ModificationDateFinFonctionPPEDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("ajout_statut_proche_ppe")
object AjoutStatutProchePPEDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("suppression_statut_proche_ppe")
object SuppressionStatutProchePPEDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("modification_lien_parente")
object ModificationLienParenteDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("modification_fonction_proche_ppe")
object ModificationFonctionProchePPEDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("ajout_vigilance")
object AjoutVigilanceDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("suppression_vigilance")
object SuppressionVigilanceDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("ajout_motif_vigilance")
data class AjoutMotifVigilanceDto(val motifs: List<MotifVigilanceDto>) : ModificationConnaissanceClientDto

@Serializable
@SerialName("suppression_motif_vigilance")
object SuppressionMotifVigilanceDto : ModificationConnaissanceClientDto

@Serializable
@SerialName("modification_motif_vigilance")
data class ModificationMotifVigilanceDto(val motifs: List<MotifVigilanceDto>) : ModificationConnaissanceClientDto

@Serializable
@SerialName("synthese_modifications")
data class SyntheseModificationDto(
    val traceAudit: TraceAuditDto,
    val modifications: Set<ModificationConnaissanceClientDto>,
)

fun ModificationConnaissanceClient.toDto(): ModificationConnaissanceClientDto = when (this) {
    is AjoutStatutPPE -> AjoutStatutPPEDto
    is SuppressionStatutPPE -> SuppressionStatutPPEDto
    is ModificationFonctionPPE -> ModificationFonctionPPEDto
    is ModificationDateFinFonctionPPE -> ModificationDateFinFonctionPPEDto
    is AjoutStatutProchePPE -> AjoutStatutProchePPEDto
    is SuppressionStatutProchePPE -> SuppressionStatutProchePPEDto
    is ModificationLienParente -> ModificationLienParenteDto
    is ModificationFonctionProchePPE -> ModificationFonctionProchePPEDto
    is AjoutVigilance -> AjoutVigilanceDto
    is SuppressionVigilance -> SuppressionVigilanceDto
    is AjoutMotifVigilance -> AjoutMotifVigilanceDto(
        this.motifs.map { MotifVigilanceDto.valueOf(it.name) },
    )

    is SuppressionMotifVigilance -> SuppressionMotifVigilanceDto
    is ModificationMotifVigilance -> ModificationMotifVigilanceDto(
        this.motifs.map { MotifVigilanceDto.valueOf(it.name) },
    )
}

fun SyntheseModifications.toDto(): SyntheseModificationDto = SyntheseModificationDto(
    traceAudit = this.traceAudit.toDto(),
    modifications = this.modifications.map { it.toDto() }.toSet(),
)

fun ModificationConnaissanceClientDto.toDomain(): ModificationConnaissanceClient = when (this) {
    is AjoutStatutPPEDto -> AjoutStatutPPE
    is SuppressionStatutPPEDto -> SuppressionStatutPPE
    is ModificationFonctionPPEDto -> ModificationFonctionPPE
    is ModificationDateFinFonctionPPEDto -> ModificationDateFinFonctionPPE
    is AjoutStatutProchePPEDto -> AjoutStatutProchePPE
    is SuppressionStatutProchePPEDto -> SuppressionStatutProchePPE
    is ModificationLienParenteDto -> ModificationLienParente
    is ModificationFonctionProchePPEDto -> ModificationFonctionProchePPE
    is AjoutVigilanceDto -> AjoutVigilance
    is SuppressionVigilanceDto -> SuppressionVigilance
    is AjoutMotifVigilanceDto -> AjoutMotifVigilance(
        this.motifs.map { MotifVigilance.valueOf(it.name) },
    )

    is SuppressionMotifVigilanceDto -> SuppressionMotifVigilance
    is ModificationMotifVigilanceDto -> ModificationMotifVigilance(
        this.motifs.map { MotifVigilance.valueOf(it.name) },
    )
}

fun SyntheseModificationDto.toDomain(): SyntheseModifications = SyntheseModifications(
    traceAudit = this.traceAudit.toDomain(),
    modifications = this.modifications.map { it.toDomain() }.toSet(),
)
