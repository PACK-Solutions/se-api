package com.ps.personne.rest.routes

import com.ps.framework.cqrs.bus.ContextProvider
import com.ps.framework.cqrs.bus.command.CommandBus
import com.ps.framework.cqrs.bus.query.QueryBus
import com.ps.personne.model.IdPersonne
import com.ps.personne.rest.dto.request.ConnaissanceClientDto
import com.ps.personne.rest.dto.request.toDto
import com.ps.personne.usecases.EnregistrerConnnaissanceClientCommand
import com.ps.personne.usecases.RecupererConnaissanceClientQuery
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.util.getOrFail

internal const val MESSAGE_PARAMETRE_MANQUANT = "le query parameter %s est manquant"

val logger = KotlinLogging.logger {}

/**
 * Configure connaissance client check routes
 */
fun Application.configureConnaissanceClientRoutes(queryBus: QueryBus, commandBus: CommandBus) {
    routing {
        route("personnes") {
            route("{idPersonne}") {
                route("connaissance-client") {
                    get(getConnaissanceClient(queryBus))
                    post(saveConnaissanceClient(commandBus))
                    put(saveConnaissanceClient(commandBus))
                }
            }
        }
    }
}

private fun getConnaissanceClient(queryBus: QueryBus): suspend RoutingContext.() -> Unit = {
    val idPersonne = call.parameters.getOrFail<Long>("idPersonne")
    val connaissanceClient = queryBus.dispatchThrowing(
        RecupererConnaissanceClientQuery(IdPersonne(idPersonne)),
        ContextProvider.Coroutine.current(),
    )

    call.respond(connaissanceClient.toDto())
}

private fun saveConnaissanceClient(commandBus: CommandBus): suspend RoutingContext.() -> Unit = {
    val idPersonne = IdPersonne(call.parameters.getOrFail<Long>("idPersonne"))
    val connaissanceClientDto = call.receive<ConnaissanceClientDto>()
    val connaissanceClient = connaissanceClientDto.toDomain(idPersonne)

    commandBus.dispatchThrowing(
        EnregistrerConnnaissanceClientCommand(
            idPersonne,
            connaissanceClient.statutPPE,
            connaissanceClient.statutProchePPE,
            connaissanceClient.vigilance,
        ),
        ContextProvider.Coroutine.current(),
    )

    call.respond(HttpStatusCode.OK, connaissanceClientDto)
}
