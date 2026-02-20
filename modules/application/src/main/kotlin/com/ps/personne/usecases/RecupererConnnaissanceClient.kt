package com.ps.personne.usecases

import com.github.michaelbull.result.fold
import com.ps.framework.cqrs.bus.Context
import com.ps.framework.cqrs.bus.query.Query
import com.ps.framework.cqrs.bus.query.QueryHandler
import com.ps.framework.cqrs.bus.query.QueryResult
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.IdPersonne
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.ConnaissanceClientRepositoryError

class RecupererConnnaissanceClientHandler(val repository: ConnaissanceClientRepository) : QueryHandler<RecupererConnaissanceClientQuery> {
    override fun handle(context: Context, query: RecupererConnaissanceClientQuery): QueryResult<ConnaissanceClient, Nothing> =
        repository.recuperer(query.idPersonne)
            .fold(
                { QueryResult.Success(it) },
                {
                    when (it) {
                        is ConnaissanceClientRepositoryError.PersonneNonTrouvee -> QueryResult.Success(
                            ConnaissanceClient.vierge(query.idPersonne),
                        )
                    }
                },
            )
}

data class RecupererConnaissanceClientQuery(
    val idPersonne: IdPersonne,
) : Query<ConnaissanceClient, Nothing>
