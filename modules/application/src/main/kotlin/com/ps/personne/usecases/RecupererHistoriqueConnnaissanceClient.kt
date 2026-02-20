package com.ps.personne.usecases

import com.ps.framework.components.history.HistoryEvent
import com.ps.framework.components.history.HistoryEventRepository
import com.ps.framework.components.history.IdObjet
import com.ps.framework.cqrs.bus.Context
import com.ps.framework.cqrs.bus.query.Query
import com.ps.framework.cqrs.bus.query.QueryHandler
import com.ps.framework.cqrs.bus.query.QueryResult
import com.ps.personne.historique.ConnaissanceClientHistoryProjection
import com.ps.personne.model.IdPersonne

class RecupererHistoriqueConnnaissanceClientHandler(val repository: HistoryEventRepository) :
    QueryHandler<RecupererHistoriqueConnaissanceClientQuery> {
    override fun handle(context: Context, query: RecupererHistoriqueConnaissanceClientQuery): QueryResult<List<HistoryEvent>, Nothing> = QueryResult.Success(
        repository.get(ConnaissanceClientHistoryProjection.typeObjet, IdObjet(query.idPersonne.id.toString()))
            .sortedBy(HistoryEvent::occurredAt),
    )
}

data class RecupererHistoriqueConnaissanceClientQuery(val idPersonne: IdPersonne) : Query<List<HistoryEvent>, Nothing>
