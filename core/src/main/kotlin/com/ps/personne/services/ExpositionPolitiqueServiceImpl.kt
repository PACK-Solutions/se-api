package com.ps.personne.services

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.Personne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.HistoriqueExpositionRepository
import com.ps.personne.ports.driving.ExpositionPolitiqueService

class ExpositionPolitiqueServiceImpl(
    private val historiqueExpositionRepository: HistoriqueExpositionRepository,
) : ExpositionPolitiqueService {

    override fun sauverEtHistoriser(
        idPersonne: IdPersonne,
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): HistoriqueExpositionPolitique? {

        // Charger l'historique
        val historiqueExpositionPolitique = historiqueExpositionRepository.recuperer(idPersonne)

        // Verifier qu'on n'ajoute pas un doublon d'exposition
        if (historiqueExpositionPolitique?.expositionCourante == expositionPolitique) return null

        // Sauvegarder la nouvelle exposition
        historiqueExpositionRepository.sauvegarder(idPersonne = idPersonne, expositionPolitique = expositionPolitique, traceAudit = traceAudit)

        // Retourner l'historique
        return historiqueExpositionRepository.recuperer(idPersonne)
    }

    override fun getHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique? {
        return historiqueExpositionRepository.recuperer(idPersonne)
    }
}
