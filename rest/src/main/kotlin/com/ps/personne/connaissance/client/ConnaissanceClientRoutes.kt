package com.ps.personne.connaissance.client

import com.github.michaelbull.result.getOrThrow
import com.ps.kommand.CommandBus
import com.ps.kommand.ContextProvider
import com.ps.kommand.QueryBus
import com.ps.personne.http.BusinessException
import com.ps.personne.http.tenantId
import com.ps.personne.kyc.dto.request.ConnaissanceClientDto
import com.ps.personne.kyc.dto.request.toDto
import com.ps.personne.kyc.dto.response.toDto
import com.ps.personne.model.IdPersonne
import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.problem.ErrorCode
import com.ps.personne.problem.respondProblem
import com.ps.personne.toResult
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
fun Application.configureConnaissanceClientRoutes(connaissanceClientService: ConnaissanceClientService, queryBus: QueryBus, commandBus: CommandBus) {
    routing {
        get("/personnes/{idPersonne}/connaissance-client", getConnaissanceClient(queryBus))
        post("/personnes/{idPersonne}/connaissance-client", saveConnaissanceClient(commandBus))
        put("/personnes/{idPersonne}/connaissance-client", saveConnaissanceClient(commandBus))
        getHistoriqueConnaissanceClientRoute(connaissanceClientService)
    }
}

private fun getConnaissanceClient(queryBus: QueryBus): suspend RoutingContext.() -> Unit = {
    val idPersonne = call.parameters.getOrFail<Long>("idPersonne")
    val connaissanceClient = queryBus.dispatch(RecupererConnaissanceClientQuery(IdPersonne(idPersonne)), ContextProvider.Coroutine.current())
        .toResult()
        .getOrThrow()
    call.respond(connaissanceClient.toDto())
}

private fun Routing.getHistoriqueConnaissanceClientRoute(connaissanceClientService: ConnaissanceClientService) {
    get("/personnes/{idPersonne}/historique-connaissance-client") {
        call.parameters["idPersonne"]?.let { idPersonne ->
            val idPersonne = IdPersonne(idPersonne.toLong())
            call.respond(HttpStatusCode.OK, connaissanceClientService.getHistorique(call.tenantId(), idPersonne).toDto())
        } ?: call.respondProblem(HttpStatusCode.BadRequest, String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"), ErrorCode("bad_request"))
    }
}

private fun saveConnaissanceClient(commandBus: CommandBus): suspend RoutingContext.() -> Unit = {
    val idPersonne = IdPersonne(call.parameters.getOrFail<Long>("idPersonne"))
    val connaissanceClientDto = call.receive<ConnaissanceClientDto>()
    val connaissanceClient = connaissanceClientDto.toDomain(idPersonne)

    commandBus.dispatch(
        EnregistrerConnnaissanceClientCommand(
            idPersonne,
            connaissanceClient.statutPPE,
            connaissanceClient.statutProchePPE,
            connaissanceClient.vigilance,
        ),
        ContextProvider.Coroutine.current(),
    ).toResult()
        .getOrThrow(::BusinessException)

    call.respond(HttpStatusCode.OK, connaissanceClientDto)

}
