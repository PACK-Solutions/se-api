package com.ps.personne.database.config

import com.ps.framework.cqrs.bus.Context
import com.ps.framework.cqrs.bus.query.Query
import com.ps.framework.cqrs.bus.query.QueryBusMiddleware
import com.ps.framework.cqrs.bus.query.QueryError
import com.ps.framework.cqrs.bus.query.QueryResult
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class QueryTransactionMiddleware(val next: QueryBusMiddleware) : QueryBusMiddleware {

    override suspend fun <Q : Query<R, E>, R, E : QueryError> handle(query: Q, context: Context): QueryResult<R, E> = newSuspendedTransaction {
        next.handle(query, context)
    }

    companion object {
        fun builder() = { next: QueryBusMiddleware? ->
            QueryTransactionMiddleware(
                next ?: error("next middleware is required for TransactionMiddleware"),
            )
        }
    }
}
