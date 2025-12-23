package com.ps.personne.ports.driven

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.HistoriqueModifications
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.SyntheseModifications

class InMemoryConnaissanceClientRepository :
    ConnaissanceClientRepository, ModificationsConnaissanceClientRepository {

    val connaissanceClients = mutableMapOf<IdPersonne, ConnaissanceClient>()
    val historiqueModifications = mutableMapOf<IdPersonne, List<SyntheseModifications>>()

    override fun recuperer(idPersonne: IdPersonne): Result<ConnaissanceClient, ConnaissanceClientRepositoryError> {
        return connaissanceClients.get(idPersonne)?.let {
            return Ok(it)
        } ?: Err(ConnaissanceClientRepositoryError.PersonneNonTrouvee)
    }

    override fun sauvegarder(connaissanceClient: ConnaissanceClient): IdPersonne {
        connaissanceClients[connaissanceClient.idPersonne] = connaissanceClient
        return connaissanceClient.idPersonne
    }

    override fun recupererHistorique(idPersonne: IdPersonne): HistoriqueModifications =
        HistoriqueModifications(idPersonne, historiqueModifications[idPersonne] ?: emptyList())

    operator fun get(idPersonne: IdPersonne) = connaissanceClients[idPersonne]
}
