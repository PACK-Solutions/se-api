package com.ps.personne.rest.routes

import com.github.michaelbull.result.getOrThrow
import com.ps.kommand.ContextProvider
import com.ps.kommand.QueryBus
import com.ps.personne.model.IdPersonne
import com.ps.personne.rest.BusinessException
import com.ps.personne.rest.dto.response.toDto
import com.ps.personne.rest.toResult
import com.ps.personne.usecases.RecupererHistoriqueConnaissanceClientQuery
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail

fun Application.configureHistoriqueRoutes(queryBus: QueryBus) {
    routing {
        get("/personnes/{idPersonne}/historique/connaissance-client", getHistoriqueConnaissanceClient(queryBus))
    }
}

private fun getHistoriqueConnaissanceClient(queryBus: QueryBus): suspend RoutingContext.() -> Unit = {
    val idPersonne = call.parameters.getOrFail<Long>("idPersonne")

    val historique = queryBus.dispatch(RecupererHistoriqueConnaissanceClientQuery(IdPersonne(idPersonne)), ContextProvider.Coroutine.current())
        .toResult()
        .getOrThrow(::BusinessException)
        .map { it.toDto() }
    call.respond(HttpStatusCode.OK, historique)
}



