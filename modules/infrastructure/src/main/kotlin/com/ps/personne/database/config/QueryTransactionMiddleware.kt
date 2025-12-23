package com.ps.personne.database.config

import com.ps.kommand.Context
import com.ps.kommand.Query
import com.ps.kommand.QueryBusMiddleware
import com.ps.kommand.QueryResult
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class QueryTransactionMiddleware(val next: QueryBusMiddleware) : QueryBusMiddleware {

    override suspend fun <Q : Query<R, E>, R, E> handle(query: Q, context: Context): QueryResult<R, E> {
        return newSuspendedTransaction {
            next.handle(query, context)
        }
    }

    companion object {
        fun builder() = { next: QueryBusMiddleware? ->
            QueryTransactionMiddleware(
                next ?: error("next middleware is required for TransactionMiddleware"),
            )
        }
    }

}
