package com.ps.personne.services

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.ModificationsConnaissanceClientRepository
import com.ps.personne.ports.driving.ConnaissanceClientService

class ConnaissanceClientServiceImpl(
    private val connaissanceClientRepository: ConnaissanceClientRepository,
    private val historiqueModificationsRepository: ModificationsConnaissanceClientRepository
) : ConnaissanceClientService {
    override fun getConnaissanceClient(idPersonne: IdPersonne) =
        connaissanceClientRepository.recuperer(idPersonne)

    override fun sauvegarderEtHistoriserModification(
        connaissanceClient: ConnaissanceClient,
        traceAudit: TraceAudit,
    ) = connaissanceClientRepository.recuperer(connaissanceClient.idPersonne)
        .map { it ?: ConnaissanceClient.vierge(connaissanceClient.idPersonne) }
        .andThen { it.appliquerModifications(connaissanceClient, traceAudit) }
        .andThen(connaissanceClientRepository::sauvegarder)

    override fun getHistoriqueConnaissanceClient(idPersonne: IdPersonne) =
        historiqueModificationsRepository.recupererHistorique(idPersonne)
}
