package com.ps.personne.usecases

import com.github.michaelbull.result.fold
import com.ps.kommand.Context
import com.ps.kommand.Query
import com.ps.kommand.QueryHandler
import com.ps.kommand.QueryResult
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.model.IdPersonne
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.ConnaissanceClientRepositoryError

class RecupererConnnaissanceClientHandler(val repository: ConnaissanceClientRepository) : QueryHandler<RecupererConnaissanceClientQuery> {
    override fun handle(
        context: Context,
        query: RecupererConnaissanceClientQuery,
    ): QueryResult<ConnaissanceClient, ConnaissanceClientError> {
        return repository.recuperer(query.idPersonne)
            .fold(
                { QueryResult.Success(it) },
                {
                    when (it) {
                        is ConnaissanceClientRepositoryError.PersonneNonTrouvee -> QueryResult.Success(ConnaissanceClient.vierge(query.idPersonne))
                    }
                },
            )
    }

}

data class RecupererConnaissanceClientQuery(
    val idPersonne: IdPersonne,
) : Query<ConnaissanceClient, ConnaissanceClientError>
