package com.ps.personne.ports.driven

import com.ps.personne.model.*

class InMemoryConnaissanceClientRepository : ConnaissanceClientRepository, ModificationsConnaissanceClientRepository {

    val connaissanceClients = mutableMapOf<IdPersonne, ConnaissanceClient>()
    val historiqueModifications = mutableMapOf<IdPersonne, List<SyntheseModifications>>()

    override fun recuperer(idPersonne: IdPersonne): ConnaissanceClient? {
        return connaissanceClients.getOrDefault(idPersonne, null)
    }

    override fun sauvegarder(connaissanceClient: ConnaissanceClient): IdPersonne {
        connaissanceClients[connaissanceClient.idPersonne] = ConnaissanceClient(
            idPersonne = connaissanceClient.idPersonne,
            statutPPE = connaissanceClient.statutPPE,
            statutProchePPE = connaissanceClient.statutProchePPE,
            vigilance = connaissanceClient.vigilance,
        )

        connaissanceClient.modification?.let {
            historiqueModifications[connaissanceClient.idPersonne] =
                listOf(it) + (historiqueModifications[connaissanceClient.idPersonne] ?: emptyList())
        }

        return connaissanceClient.idPersonne
    }

    override fun recupererHistorique(idPersonne: IdPersonne): HistoriqueModifications =
        HistoriqueModifications(idPersonne, historiqueModifications[idPersonne] ?: emptyList())
}
