package com.ps.personne.config

import com.ps.kommand.BasicQueryBus
import com.ps.kommand.QueryBus
import com.ps.kommand.QueryHandler
import com.ps.kommand.middleware.QueryDispatcherMiddleware
import com.ps.personne.database.config.QueryTransactionMiddleware

fun configureQueryBus(queryHandlers: List<QueryHandler<*>>): QueryBus {
    return BasicQueryBus(
        linkedSetOf(
            QueryTransactionMiddleware.builder(),
            QueryDispatcherMiddleware.builder(queryHandlers),
        ),
    )
}
