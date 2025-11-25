package com.ps.personne.ports.driven

import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.HistoriqueModifications
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.SyntheseModifications

class InMemoryConnaissanceClientRepository : ConnaissanceClientRepository, ModificationsConnaissanceClientRepository {

    val connaissanceClients = mutableMapOf<IdPersonne, ConnaissanceClient>()
    val historiqueModifications = mutableMapOf<IdPersonne, List<SyntheseModifications>>()

    override fun recuperer(tenantId: String, idPersonne: IdPersonne): ConnaissanceClient {
        return connaissanceClients.getOrDefault(idPersonne, ConnaissanceClient.vierge(idPersonne))
    }

    override fun sauvegarder(tenantId: String, connaissanceClient: ConnaissanceClient): IdPersonne {
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

    override fun recupererHistorique(tenantId: String, idPersonne: IdPersonne): HistoriqueModifications =
        HistoriqueModifications(idPersonne, historiqueModifications[idPersonne] ?: emptyList())
}
