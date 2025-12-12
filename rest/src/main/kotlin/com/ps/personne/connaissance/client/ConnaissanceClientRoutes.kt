package com.ps.personne.connaissance.client

import com.github.michaelbull.result.*
import com.ps.kommand.CommandBus
import com.ps.kommand.CommandResult
import com.ps.kommand.ContextProvider
import com.ps.kommand.QueryBus
import com.ps.personne.http.login
import com.ps.personne.http.tenantId
import com.ps.personne.kyc.dto.request.ConnaissanceClientDto
import com.ps.personne.kyc.dto.request.toDto
import com.ps.personne.kyc.dto.response.toDto
import com.ps.personne.model.*
import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.problem.ErrorCodes
import com.ps.personne.problem.respondProblem
import com.ps.personne.toResult
import com.ps.personne.usecases.EnregistrerConnnaissanceClientCommand
import com.ps.personne.usecases.RecupererConnaissanceClientQuery
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.plugins.ParameterConversionException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.util.getOrFail
import java.time.Instant

internal const val MESSAGE_PARAMETRE_MANQUANT = "le query parameter %s est manquant"

val logger = KotlinLogging.logger {}

/**
 * Configure connaissance client check routes
 */
fun Application.configureConnaissanceClientRoutes(connaissanceClientService: ConnaissanceClientService, queryBus: QueryBus, commandBus: CommandBus) {
    routing {
        getConnaissanceClientRoute(queryBus, connaissanceClientService)
        getHistoriqueConnaissanceClientRoute(connaissanceClientService)
        createConnaissanceClientRoute(commandBus, connaissanceClientService)
        updateConnaissanceClientRoute(connaissanceClientService)
    }
}

private fun Routing.getConnaissanceClientRoute(queryBus: QueryBus, connaissanceClientService: ConnaissanceClientService) {
    get("/personnes/{idPersonne}/connaissance-client") {
        val q = queryBus.dispatch(RecupererConnaissanceClientQuery(IdPersonne(1L)), ContextProvider.Coroutine.current())
        runCatching { call.parameters.getOrFail<Long>("idPersonne") }
            .andThen { queryBus.dispatch(RecupererConnaissanceClientQuery(IdPersonne(it)), ContextProvider.Coroutine.current()).toResult() }
            .fold(
                { call.respond(it.toDto()) },
                {
                    when (it) {
                        is ParameterConversionException -> call.respondProblem(HttpStatusCode.NotFound, it.message, ErrorCodes.NOT_FOUND)
                        else -> call.respondProblem(
                            HttpStatusCode.InternalServerError, "An unexpected error occured", ErrorCodes.INTERNAL_SERVER_ERROR,
                        )
                    }
                },
            )
    }
}

private fun Routing.getHistoriqueConnaissanceClientRoute(connaissanceClientService: ConnaissanceClientService) {
    get("/personnes/{idPersonne}/historique-connaissance-client") {
        call.parameters["idPersonne"]?.let { idPersonne ->
            val idPersonne = IdPersonne(idPersonne.toLong())
            call.respond(HttpStatusCode.OK, connaissanceClientService.getHistorique(call.tenantId(), idPersonne).toDto())
        } ?: call.respondProblem(HttpStatusCode.BadRequest, String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"), ErrorCodes.BAD_REQUEST)
    }
}

private fun Routing.createConnaissanceClientRoute(commandBus: CommandBus, connaissanceClientService: ConnaissanceClientService) {
    post("/personnes/{idPersonne}/connaissance-client") {
        val connaissanceClientDto = call.receive<ConnaissanceClientDto>()
        runCatching { IdPersonne(call.parameters.getOrFail<Long>("idPersonne")) }
            .map { it to connaissanceClientDto.toDomain(it) }
            .andThen { (idPersonne, connaissanceClient) ->
                commandBus.dispatch(
                    EnregistrerConnnaissanceClientCommand(
                        idPersonne,
                        connaissanceClient.statutPPE,
                        connaissanceClient.statutProchePPE,
                        connaissanceClient.vigilance,
                    ),
                    ContextProvider.Coroutine.current(),
                ).toResult()
            }.fold(
                { call.respond(HttpStatusCode.OK, connaissanceClientDto) },
                {
                    when (it) {
                        is ParameterConversionException -> call.respondProblem(HttpStatusCode.NotFound, "L'id personne est invalide", ErrorCodes.NOT_FOUND)
                        is ConnaissanceClientError.AucuneModification -> call.respond(HttpStatusCode.NotModified)
                        is ConnaissanceClientError.VigilanceRenforceeObligatoire ->
                            call.respondProblem(
                                HttpStatusCode.BadRequest, "La vigilance renforcée est obligatoire pour un PPE ou un proche PPE", ErrorCodes.BAD_REQUEST,
                            )
                    }
                },
            )
        call.parameters["idPersonne"]?.let { idPersonne ->
            val idPersonne = IdPersonne(idPersonne.toLong())
            val connaissanceClient = connaissanceClientDto.toDomain(idPersonne)
            val login = call.login()

            val result = commandBus.dispatch(
                EnregistrerConnnaissanceClientCommand(
                    idPersonne,
                    connaissanceClient.statutPPE,
                    connaissanceClient.statutProchePPE,
                    connaissanceClient.vigilance,
                ),
                ContextProvider.Coroutine.current(),
            )
            when (result) {
                is CommandResult.Success<*> -> call.respond(HttpStatusCode.OK, connaissanceClientDto)
                is CommandResult.Failure<*> -> when (result.error) {
                    is ConnaissanceClientError.AucuneModification -> {
                        logger.warn { "Aucune modification de la connaissance client pour l'utilisateur $login sur la personne $idPersonne" }
                        call.respond(HttpStatusCode.NotModified)
                    }

                    is ConnaissanceClientError.VigilanceRenforceeObligatoire ->
                        call.respondProblem(
                            HttpStatusCode.BadRequest, "La vigilance renforcée est obligatoire pour un PPE ou un proche PPE", ErrorCodes.BAD_REQUEST,
                        )
                }
            }

//            connaissanceClientService.sauvegarderEtHistoriserModification(
//                call.tenantId(),
//                connaissanceClient,
//                TraceAudit(user = User(login), date = Instant.now(), TypeOperation.MODIFICATION),
//            )
//                .onSuccess { }
//                .onFailure {
//                    when (it) {
//                        is ConnaissanceClientError.AucuneModification -> {
//                            logger.warn { "Aucune modification de la connaissance client pour l'utilisateur $login sur la personne $idPersonne" }
//                            call.respond(HttpStatusCode.NotModified)
//                        }
//
//                        is ConnaissanceClientError.VigilanceRenforceeObligatoire ->
//                            call.respondProblem(
//                                HttpStatusCode.InternalServerError,
//                                Problem.of(
//                                    httpStatusCode = HttpStatusCode.InternalServerError,
//                                    problemDetail = it.message,
//                                    code = ErrorCodes.BAD_REQUEST,
//                                ),
//                            )
//                    }
//                }
        } ?: call.respondProblem(
            HttpStatusCode.BadRequest, String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"), ErrorCodes.BAD_REQUEST,
        )
    }
}

private fun Routing.updateConnaissanceClientRoute(connaissanceClientService: ConnaissanceClientService) {
    put("/personnes/{idPersonne}/connaissance-client") {
        val connaissanceClientDto = call.receive<ConnaissanceClientDto>()

        call.parameters["idPersonne"]?.let { idPersonne ->
            val idPersonne = IdPersonne(idPersonne.toLong())
            val connaissanceClient = connaissanceClientDto.toDomain(idPersonne)
            val login = call.login()

            connaissanceClientService.sauvegarderEtHistoriserModification(
                call.tenantId(),
                connaissanceClient,
                TraceAudit(user = User(login), date = Instant.now(), TypeOperation.CORRECTION),
            )
                .onSuccess { call.respond(HttpStatusCode.Created, connaissanceClientDto) }
                .onFailure {
                    when (it) {
                        is ConnaissanceClientError.AucuneModification -> {
                            logger.warn { "Aucune modification de la connaissance client pour l'utilisateur $login sur la personne $idPersonne" }
                            call.respond(HttpStatusCode.NotModified)
                        }

                        is ConnaissanceClientError.VigilanceRenforceeObligatoire ->
                            call.respondProblem(
                                HttpStatusCode.BadRequest, "La vigilance renforcée est obligatoire pour un PPE ou un proche PPE", ErrorCodes.BAD_REQUEST,
                            )
                    }
                }
        } ?: call.respondProblem(HttpStatusCode.BadRequest, String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"), ErrorCodes.BAD_REQUEST)
    }
}
