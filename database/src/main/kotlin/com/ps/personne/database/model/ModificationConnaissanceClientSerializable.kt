package com.ps.personne.database.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ModificationConnaissanceClientSer

@Serializable
@SerialName("ajout_statut_ppe")
object AjoutStatutPPESer : ModificationConnaissanceClientSer

@Serializable
@SerialName("suppression_statut_ppe")
object SuppressionStatutPPESer : ModificationConnaissanceClientSer

@Serializable
@SerialName("modification_fonction_ppe")
object ModificationFonctionPPESer : ModificationConnaissanceClientSer

@Serializable
@SerialName("modification_date_fin_fonction_ppe")
object ModificationDateFinFonctionPPESer : ModificationConnaissanceClientSer

@Serializable
@SerialName("ajout_statut_proche_ppe")
object AjoutStatutProchePPESer : ModificationConnaissanceClientSer

@Serializable
@SerialName("suppression_statut_proche_ppe")
object SuppressionStatutProchePPESer : ModificationConnaissanceClientSer

@Serializable
@SerialName("modification_lien_parente")
object ModificationLienParenteSer : ModificationConnaissanceClientSer

@Serializable
@SerialName("modification_fonction_proche_ppe")
object ModificationFonctionProchePPESer : ModificationConnaissanceClientSer

@Serializable
@SerialName("ajout_vigilance")
object AjoutVigilanceSer : ModificationConnaissanceClientSer

@Serializable
@SerialName("suppression_vigilance")
object SuppressionVigilanceSer : ModificationConnaissanceClientSer

@Serializable
@SerialName("ajout_motif_vigilance")
data class AjoutMotifVigilanceSer(val motifs: List<MotifVigilanceSer>) : ModificationConnaissanceClientSer

@Serializable
@SerialName("suppression_motif_vigilance")
object SuppressionMotifVigilanceSer : ModificationConnaissanceClientSer

@Serializable
@SerialName("modification_motif_vigilance")
data class ModificationMotifVigilanceSer(val motifs: List<MotifVigilanceSer>) : ModificationConnaissanceClientSer

@Serializable
@SerialName("synthese_modifications")
data class SyntheseModificationSer(
    val traceAudit: TraceAuditSer,
    val modifications: Set<ModificationConnaissanceClientSer>
)
