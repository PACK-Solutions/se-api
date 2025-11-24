package com.ps.personne.model

sealed interface ModificationConnaissanceClient
object AjoutStatutPPE : ModificationConnaissanceClient
object SuppressionStatutPPE : ModificationConnaissanceClient
object ModificationFonctionPPE : ModificationConnaissanceClient
object ModificationDateFinFonctionPPE : ModificationConnaissanceClient

object AjoutStatutProchePPE : ModificationConnaissanceClient
object SuppressionStatutProchePPE : ModificationConnaissanceClient
object ModificationLienParente : ModificationConnaissanceClient
object ModificationFonctionProchePPE : ModificationConnaissanceClient

object AjoutVigilance : ModificationConnaissanceClient
object SuppressionVigilance : ModificationConnaissanceClient
data class AjoutMotifVigilance(val motifs: List<MotifVigilance>) : ModificationConnaissanceClient
object SuppressionMotifVigilance : ModificationConnaissanceClient
data class ModificationMotifVigilance(val motifs: List<MotifVigilance>) : ModificationConnaissanceClient

data class SyntheseModifications(
    val traceAudit: TraceAudit,
    val modifications: Set<ModificationConnaissanceClient>,
)

data class HistoriqueModifications(
    val idPersonne: IdPersonne,
    val entreesHistorique: List<SyntheseModifications>,
)
