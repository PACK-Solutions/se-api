package com.ps.personne.services

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.Personne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.HistoriqueExpositionRepository
import com.ps.personne.ports.driven.PersonneRepository
import com.ps.personne.ports.driving.ExpositionPolitiqueService

class ExpositionPolitiqueServiceImpl(
    private val personneRepository: PersonneRepository,
    private val historiqueExpositionRepository: HistoriqueExpositionRepository,
) : ExpositionPolitiqueService {

    override fun sauverEtHistoriser(
        idPersonne: IdPersonne,
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): Pair<Personne, HistoriqueExpositionPolitique>? {
        val personne = personneRepository.sauvegarder(idPersonne, expositionPolitique, traceAudit)
        historiqueExpositionRepository.sauvegarder(personne.expositionPolitique, idPersonne)
        val historique = getHistorique(idPersonne)
        return historique?.let {
            personne to it
        }
    }

    override fun getHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique? {
        return historiqueExpositionRepository.recupererHistorique(idPersonne)
    }
}
