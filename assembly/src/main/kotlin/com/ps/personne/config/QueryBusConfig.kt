package com.ps.personne.config

import com.ps.kommand.BasicQueryBus
import com.ps.kommand.QueryBus
import com.ps.kommand.QueryHandler
import com.ps.kommand.middleware.QueryDispatcherMiddleware

fun configureQueryBus(queryHandlers: List<QueryHandler<*>>): QueryBus {
    return BasicQueryBus(
        linkedSetOf(
            QueryDispatcherMiddleware.builder(queryHandlers),
        ),
    )
}


