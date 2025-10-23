package com.ps.personne.model

@ConsistentCopyVisibility
data class HistoriqueExpositionPolitique private constructor(
    val idPersonne: IdPersonne,
    private val entrees: Set<EntreeHistoriqueExpositionPolitique>,
    val expositionCourante: ExpositionPolitique?,
) : Iterable<EntreeHistoriqueExpositionPolitique> by entrees {
    fun ajouterEntree(expositionPolitique: ExpositionPolitique, traceAudit: TraceAudit): HistoriqueExpositionPolitique {
        expositionCourante?.cloturer(traceAudit.date)
        // TODO : Ensure order of entries
        return this.copy(
            entrees = entrees + EntreeHistoriqueExpositionPolitique(expositionPolitique, traceAudit),
            expositionCourante = expositionPolitique,
        )
    }

    companion object {
        fun vierge(idPersonne: IdPersonne): HistoriqueExpositionPolitique {
            return HistoriqueExpositionPolitique(idPersonne, LinkedHashSet(), null)
        }
    }
}

data class EntreeHistoriqueExpositionPolitique(
    val expositionPolitique: ExpositionPolitique,
    val traceAudit: TraceAudit,
)
