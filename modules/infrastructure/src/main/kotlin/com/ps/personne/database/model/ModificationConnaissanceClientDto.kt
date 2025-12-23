package com.ps.personne.database.model

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

fun com.ps.personne.model.ModificationConnaissanceClient.toDto(): ModificationConnaissanceClientDto = when (this) {
    is com.ps.personne.model.AjoutStatutPPE -> AjoutStatutPPEDto
    is com.ps.personne.model.SuppressionStatutPPE -> SuppressionStatutPPEDto
    is com.ps.personne.model.ModificationFonctionPPE -> ModificationFonctionPPEDto
    is com.ps.personne.model.ModificationDateFinFonctionPPE -> ModificationDateFinFonctionPPEDto
    is com.ps.personne.model.AjoutStatutProchePPE -> AjoutStatutProchePPEDto
    is com.ps.personne.model.SuppressionStatutProchePPE -> SuppressionStatutProchePPEDto
    is com.ps.personne.model.ModificationLienParente -> ModificationLienParenteDto
    is com.ps.personne.model.ModificationFonctionProchePPE -> ModificationFonctionProchePPEDto
    is com.ps.personne.model.AjoutVigilance -> AjoutVigilanceDto
    is com.ps.personne.model.SuppressionVigilance -> SuppressionVigilanceDto
    is com.ps.personne.model.AjoutMotifVigilance -> AjoutMotifVigilanceDto(
        this.motifs.map { MotifVigilanceDto.valueOf(it.name) },
    )

    is com.ps.personne.model.SuppressionMotifVigilance -> SuppressionMotifVigilanceDto
    is com.ps.personne.model.ModificationMotifVigilance -> ModificationMotifVigilanceDto(
        this.motifs.map { MotifVigilanceDto.valueOf(it.name) },
    )
}

fun com.ps.personne.model.SyntheseModifications.toDto(): SyntheseModificationDto = SyntheseModificationDto(
    traceAudit = this.traceAudit.toDto(),
    modifications = this.modifications.map { it.toDto() }.toSet(),
)

fun ModificationConnaissanceClientDto.toDomain(): com.ps.personne.model.ModificationConnaissanceClient = when (this) {
    is AjoutStatutPPEDto -> _root_ide_package_.com.ps.personne.model.AjoutStatutPPE
    is SuppressionStatutPPEDto -> _root_ide_package_.com.ps.personne.model.SuppressionStatutPPE
    is ModificationFonctionPPEDto -> _root_ide_package_.com.ps.personne.model.ModificationFonctionPPE
    is ModificationDateFinFonctionPPEDto -> _root_ide_package_.com.ps.personne.model.ModificationDateFinFonctionPPE
    is AjoutStatutProchePPEDto -> _root_ide_package_.com.ps.personne.model.AjoutStatutProchePPE
    is SuppressionStatutProchePPEDto -> _root_ide_package_.com.ps.personne.model.SuppressionStatutProchePPE
    is ModificationLienParenteDto -> _root_ide_package_.com.ps.personne.model.ModificationLienParente
    is ModificationFonctionProchePPEDto -> _root_ide_package_.com.ps.personne.model.ModificationFonctionProchePPE
    is AjoutVigilanceDto -> _root_ide_package_.com.ps.personne.model.AjoutVigilance
    is SuppressionVigilanceDto -> _root_ide_package_.com.ps.personne.model.SuppressionVigilance
    is AjoutMotifVigilanceDto -> _root_ide_package_.com.ps.personne.model.AjoutMotifVigilance(
        this.motifs.map { _root_ide_package_.com.ps.personne.model.MotifVigilance.valueOf(it.name) },
    )

    is SuppressionMotifVigilanceDto -> _root_ide_package_.com.ps.personne.model.SuppressionMotifVigilance
    is ModificationMotifVigilanceDto -> _root_ide_package_.com.ps.personne.model.ModificationMotifVigilance(
        this.motifs.map { _root_ide_package_.com.ps.personne.model.MotifVigilance.valueOf(it.name) },
    )
}

fun SyntheseModificationDto.toDomain(): com.ps.personne.model.SyntheseModifications =
    _root_ide_package_.com.ps.personne.model.SyntheseModifications(
        traceAudit = this.traceAudit.toDomain(),
        modifications = this.modifications.map { it.toDomain() }.toSet(),
    )
