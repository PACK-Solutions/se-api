package com.ps.personne.services

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.PersonneRepository
import com.ps.personne.ports.driving.ExpositionPolitiqueService

class ExpositionPolitiqueServiceImpl(private val personneRepository: PersonneRepository) : ExpositionPolitiqueService {

    override fun sauverEtHistoriser(
        idPersonne: IdPersonne,
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): Boolean {
        personneRepository.sauvegarder(idPersonne, expositionPolitique, traceAudit)
        return true
    }

    override fun getHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique {
        return personneRepository.recupererHistorique(idPersonne)
    }
}
