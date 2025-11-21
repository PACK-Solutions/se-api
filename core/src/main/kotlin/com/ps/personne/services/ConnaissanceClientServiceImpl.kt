package com.ps.personne.services

import com.github.michaelbull.result.map
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.ModificationsConnaissanceClientRepository
import com.ps.personne.ports.driving.ConnaissanceClientService

class ConnaissanceClientServiceImpl(
    private val connaissanceClientRepository: ConnaissanceClientRepository,
    private val historiqueModificationsRepository: ModificationsConnaissanceClientRepository,
) : ConnaissanceClientService {
    override fun getConnaissanceClient(idPersonne: IdPersonne) =
        connaissanceClientRepository.recuperer(idPersonne)

    override fun sauvegarderEtHistoriserModification(
        connaissanceClient: ConnaissanceClient,
        traceAudit: TraceAudit,
    ) = (connaissanceClientRepository.recuperer(connaissanceClient.idPersonne) ?: ConnaissanceClient.vierge(connaissanceClient.idPersonne))
        .appliquerModifications(connaissanceClient, traceAudit)
        .map(connaissanceClientRepository::sauvegarder)

    override fun getHistorique(idPersonne: IdPersonne) =
        historiqueModificationsRepository.recupererHistorique(idPersonne)
}
