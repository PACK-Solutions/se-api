package com.ps.personne.database

import com.ps.personne.model.AvecVigilanceRenforcee
import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.Mandat
import com.ps.personne.model.MotifVigilance
import com.ps.personne.model.Personne
import com.ps.personne.model.SansVigilanceRenforcee
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.PersonneRepository
import com.ps.personne.rest.persistence.ExpositionPolitiqueTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Exposed implementation of PersonRepository
 */
class ExposedPersonRepository : PersonneRepository {

    override fun sauvegarder(idPersonne: IdPersonne, expositionPolitique: ExpositionPolitique, traceAudit: TraceAudit): Personne {
        TODO("Not yet implemented")
    }

    override fun recupererExpositionPolitiqueCourante(idPersonne: IdPersonne) = transaction {
        ExpositionPolitiqueTable
            .selectAll()
            .where { ExpositionPolitiqueTable.personId eq idPersonne.id }
            .singleOrNull()
            ?.let(::mapToExpositionPolitique)
    }

    private fun mapToExpositionPolitique(expositionPolitiqueRow: ResultRow): ExpositionPolitique {
        val id = IdExpositionPolitique(expositionPolitiqueRow[ExpositionPolitiqueTable.id])
        val dateDebut = expositionPolitiqueRow[ExpositionPolitiqueTable.dateDebut]

        val vigilance = if (expositionPolitiqueRow[ExpositionPolitiqueTable.vigilanceRenforcee]) {
            val motif = expositionPolitiqueRow[ExpositionPolitiqueTable.motifVigilance]
                ?: MotifVigilance.SANS_JUSTIFICATION
            AvecVigilanceRenforcee(motif)
        } else {
            SansVigilanceRenforcee
        }

        return when (expositionPolitiqueRow[ExpositionPolitiqueTable.type]) {
            "PPE" -> {
                val fonction = expositionPolitiqueRow[ExpositionPolitiqueTable.mandatFonction]
                    ?: error("mandat_fonction must not be null for PPE")
                val mandatDateFin = expositionPolitiqueRow[ExpositionPolitiqueTable.mandatDateFin]
                val mandat = Mandat(fonction, mandatDateFin)

                // For PPE, vigilance must be reinforced
                val vig = when (vigilance) {
                    is AvecVigilanceRenforcee -> vigilance
                    else -> AvecVigilanceRenforcee(MotifVigilance.SANS_JUSTIFICATION)
                }
                ExpositionPolitique.Ppe(
                    idExpositionPolitique = id,
                    dateDebut = dateDebut,
                    vigilance = vig,
                    mandat = mandat,
                )
            }

            "PROCHE_PPE" -> {
                val fonction = expositionPolitiqueRow[ExpositionPolitiqueTable.mandatFonction]
                    ?: error("mandat_fonction must not be null for PROCHE_PPE")
                val mandatDateFin = expositionPolitiqueRow[ExpositionPolitiqueTable.mandatDateFin]
                val mandat = Mandat(fonction, mandatDateFin)
                val lien = expositionPolitiqueRow[ExpositionPolitiqueTable.lienParente]
                    ?: error("lien_parente must not be null for PROCHE_PPE")

                ExpositionPolitique.ProchePpe(
                    idExpositionPolitique = id,
                    dateDebut = dateDebut,
                    vigilance = vigilance,
                    lienParente = lien,
                    mandat = mandat,
                )
            }

            else -> {
                ExpositionPolitique.Standard(
                    idExpositionPolitique = id,
                    dateDebut = dateDebut,
                    vigilance = vigilance,
                )
            }
        }
    }

    override fun recupererHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique {
        TODO("Not yet implemented")
    }
}
