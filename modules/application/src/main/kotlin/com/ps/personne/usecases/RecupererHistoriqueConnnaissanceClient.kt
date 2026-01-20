package com.ps.personne.usecases

import com.ps.kommand.Context
import com.ps.kommand.Query
import com.ps.kommand.QueryHandler
import com.ps.kommand.QueryResult
import com.ps.personne.historique.ConnaissanceClientHistoriqueProjection
import com.ps.personne.historique.EntreeHistorique
import com.ps.personne.historique.HistoriqueRepository
import com.ps.personne.historique.IdObjet
import com.ps.personne.model.IdPersonne

class RecupererHistoriqueConnnaissanceClientHandler(val repository: HistoriqueRepository) : QueryHandler<RecupererHistoriqueConnaissanceClientQuery> {
    override fun handle(
        context: Context,
        query: RecupererHistoriqueConnaissanceClientQuery,
    ): QueryResult<List<EntreeHistorique>, Nothing> {
        return QueryResult.Success(
            repository.get(ConnaissanceClientHistoriqueProjection.typeObjet, IdObjet(query.idPersonne.id.toString()))
                .sortedBy(EntreeHistorique::occurredAt),
        )


    }
}

data class RecupererHistoriqueConnaissanceClientQuery(
    val idPersonne: IdPersonne,
) : Query<List<EntreeHistorique>, Nothing>
