package com.ps.personne.services

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.HistoriqueExpositionPolitiqueRepository
import com.ps.personne.ports.driving.ExpositionPolitiqueService

class ExpositionPolitiqueServiceImpl(
    private val historiqueExpositionPolitiqueRepository: HistoriqueExpositionPolitiqueRepository,
) : ExpositionPolitiqueService {

    override fun sauverEtHistoriser(
        idPersonne: IdPersonne,
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): HistoriqueExpositionPolitique? {
        val historiqueExpositionPolitique = historiqueExpositionPolitiqueRepository.recuperer(idPersonne)
            ?: HistoriqueExpositionPolitique.vierge(idPersonne)

        return historiqueExpositionPolitiqueRepository.sauvegarder(
            historiqueExpositionPolitique.ajouterEntree(expositionPolitique, traceAudit)
        )
    }

    override fun getHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique? {
        return historiqueExpositionPolitiqueRepository.recuperer(idPersonne)
    }
}
