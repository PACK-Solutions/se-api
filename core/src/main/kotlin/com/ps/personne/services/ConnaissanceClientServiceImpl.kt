package com.ps.personne.services

import com.github.michaelbull.result.map
import com.github.michaelbull.result.recoverIf
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.ModificationsConnaissanceClientRepository
import com.ps.personne.ports.driving.ConnaissanceClientService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.KotlinLogging.logger

val logger = KotlinLogging.logger { }

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
        .recoverIf(
            {
                it is ConnaissanceClientError.AucuneModification
            },
        ) {
            connaissanceClient.also {
                logger.warn {
                    "Aucune modification sur la connaissance client, aucun enregistrement n'est fait. Id personne : ${connaissanceClient.idPersonne.id}"
                }
            }
        }
        .map(connaissanceClientRepository::sauvegarder)

    override fun getHistorique(idPersonne: IdPersonne) =
        historiqueModificationsRepository.recupererHistorique(idPersonne)
}
