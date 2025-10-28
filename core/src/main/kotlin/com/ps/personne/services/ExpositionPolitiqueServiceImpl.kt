package com.ps.personne.services

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
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
    ) = historiqueExpositionPolitiqueRepository
        .recuperer(idPersonne)
        .map { it ?: HistoriqueExpositionPolitique.vierge(idPersonne) }
        .andThen { historique -> historique.ajouterEntree(expositionPolitique, traceAudit) }
        .andThen(historiqueExpositionPolitiqueRepository::sauvegarder)

    override fun getHistorique(idPersonne: IdPersonne) = historiqueExpositionPolitiqueRepository
        .recuperer(idPersonne)
}
