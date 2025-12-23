package com.ps.personne.usecases

import com.ps.kommand.Context
import com.ps.kommand.Query
import com.ps.kommand.QueryHandler
import com.ps.kommand.QueryResult
import com.ps.personne.database.historique.EntreeHistorique
import com.ps.personne.database.repository.HistoriqueRepository
import com.ps.personne.model.IdPersonne

class RecupererHistoriqueConnnaissanceClientHandler(val repository: HistoriqueRepository) : QueryHandler<RecupererHistoriqueConnaissanceClientQuery> {
    override fun handle(
        context: Context,
        query: RecupererHistoriqueConnaissanceClientQuery,
    ): QueryResult<List<EntreeHistorique>, Nothing> {
        error("Not yet implemented")
        //    return QueryResult.Success(repository.get("ConnaissanceClient", query.idPersonne.id.toString()))
    }

}

data class RecupererHistoriqueConnaissanceClientQuery(
    val idPersonne: IdPersonne,
) : Query<List<EntreeHistorique>, Nothing>
