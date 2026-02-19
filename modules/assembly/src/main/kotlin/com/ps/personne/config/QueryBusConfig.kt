package com.ps.personne.config

import com.ps.framework.cqrs.bus.query.BasicQueryBus
import com.ps.framework.cqrs.bus.query.QueryBus
import com.ps.framework.cqrs.bus.query.QueryHandler
import com.ps.framework.cqrs.bus.query.middleware.QueryDispatcherMiddleware
import com.ps.personne.database.config.QueryTransactionMiddleware

fun configureQueryBus(queryHandlers: List<QueryHandler<*>>): QueryBus = BasicQueryBus(
    linkedSetOf(
        QueryTransactionMiddleware.builder(),
        QueryDispatcherMiddleware.builder(queryHandlers),
    ),
)
