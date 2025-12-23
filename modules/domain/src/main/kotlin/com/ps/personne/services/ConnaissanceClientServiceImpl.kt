package com.ps.personne.services

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOr
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.ModificationsConnaissanceClientRepository
import com.ps.personne.ports.driving.ConnaissanceClientService
import io.github.oshai.kotlinlogging.KotlinLogging

val logger = KotlinLogging.logger { }

class ConnaissanceClientServiceImpl(
    private val connaissanceClientRepository: ConnaissanceClientRepository,
    private val historiqueModificationsRepository: ModificationsConnaissanceClientRepository,
) : ConnaissanceClientService {
    override fun getConnaissanceClient(tenantId: String, idPersonne: IdPersonne) =
        connaissanceClientRepository.recuperer(idPersonne).getOr(ConnaissanceClient.vierge(idPersonne))

    override fun sauvegarderEtHistoriserModification(
        tenantId: String,
        connaissanceClient: ConnaissanceClient,
        traceAudit: TraceAudit,
    ): Result<IdPersonne, ConnaissanceClientError> {
//        connaissanceClientRepository.recuperer(tenantId, connaissanceClient.idPersonne)
//            .appliquerModifications(connaissanceClient, traceAudit)
//            .map { connaissanceClientRepository.sauvegarder(tenantId, it) }
        return Ok(connaissanceClient.idPersonne)
    }

    override fun getHistorique(tenantId: String, idPersonne: IdPersonne) =
        historiqueModificationsRepository.recupererHistorique(idPersonne)
}
