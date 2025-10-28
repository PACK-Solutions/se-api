package com.ps.personne.model

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

@ConsistentCopyVisibility
data class HistoriqueExpositionPolitique private constructor(
    val idPersonne: IdPersonne,
    private val entrees: Set<EntreeHistoriqueExpositionPolitique>,
    val expositionCourante: ExpositionPolitique?,
) : Iterable<EntreeHistoriqueExpositionPolitique> by entrees {

    fun ajouterEntree(
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): Result<HistoriqueExpositionPolitique, ExpositionPolitiqueError> {

        if (expositionPolitique == expositionCourante) {
            return Err(
                ExpositionPolitiqueError.EntreeHistoriqueIdentiqueCourante(
                    message = "L'exposition politique $expositionPolitique est la même que $expositionCourante",
                ),
            )
        }

        expositionCourante?.cloturer(traceAudit.date)
        // TODO : Ensure order of entries
        return Ok(
            this.copy(
                entrees = entrees + EntreeHistoriqueExpositionPolitique(expositionPolitique, traceAudit),
                expositionCourante = expositionPolitique,
            ),
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
