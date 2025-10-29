package com.ps.personne.ports.driven

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.model.HistoriqueModifications
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.SyntheseModifications

class InMemoryConnaissanceClientRepository : ConnaissanceClientRepository, ModificationsConnaissanceClientRepository {

    val connaissanceClients = mutableMapOf<IdPersonne, ConnaissanceClient>()
    val historiqueModifications = mutableMapOf<IdPersonne, List<SyntheseModifications>>()
    override fun recuperer(idPersonne: IdPersonne): Result<ConnaissanceClient?, ConnaissanceClientError> {
        return Ok(connaissanceClients.getOrDefault(idPersonne, null))
    }

    override fun sauvegarder(connaissanceClient: ConnaissanceClient): Result<IdPersonne, ConnaissanceClientError> {
        connaissanceClients[connaissanceClient.idPersonne] = ConnaissanceClient(
            idPersonne = connaissanceClient.idPersonne,
            statutPPE = connaissanceClient.statutPPE,
            statutProchePPE = connaissanceClient.statutProchePPE,
            vigilance = connaissanceClient.vigilance
        )

        connaissanceClient.modification?.let {
            historiqueModifications[connaissanceClient.idPersonne] =
                listOf(it) + (historiqueModifications[connaissanceClient.idPersonne] ?: emptyList())
        }

        return Ok(connaissanceClient.idPersonne)
    }

    override fun recupererHistorique(idPersonne: IdPersonne): Result<HistoriqueModifications, ConnaissanceClientError> {
        return Ok(HistoriqueModifications(idPersonne, historiqueModifications[idPersonne] ?: emptyList()))
    }
}
