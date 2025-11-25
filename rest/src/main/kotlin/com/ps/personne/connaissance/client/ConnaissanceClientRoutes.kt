package com.ps.personne.connaissance.client

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.ps.personne.http.login
import com.ps.personne.http.tenantId
import com.ps.personne.kyc.dto.request.ConnaissanceClientDto
import com.ps.personne.kyc.dto.request.toDto
import com.ps.personne.kyc.dto.response.toDto
import com.ps.personne.model.*
import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.problem.ErrorCodes
import com.ps.personne.problem.Problem
import com.ps.personne.problem.respondProblem
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.time.Instant

internal const val MESSAGE_PARAMETRE_MANQUANT = "le query parameter %s est manquant"

val logger = KotlinLogging.logger {}

/**
 * Configure connaissance client check routes
 */
fun Application.configureConnaissanceClientRoutes(connaissanceClientService: ConnaissanceClientService) {
    routing {
        getConnaissanceClientRoute(connaissanceClientService)
        getHistoriqueConnaissanceClientRoute(connaissanceClientService)
        createConnaissanceClientRoute(connaissanceClientService)
        updateConnaissanceClientRoute(connaissanceClientService)
    }
}

private fun Routing.getConnaissanceClientRoute(connaissanceClientService: ConnaissanceClientService) {
    get("/personnes/{idPersonne}/connaissance-client") {
        call.parameters["idPersonne"]?.let { idPersonne ->
            val idPersonne = IdPersonne(idPersonne.toLong())
            call.respond(connaissanceClientService.getConnaissanceClient(call.tenantId(), idPersonne).toDto())
        } ?: call.respondProblem(
            HttpStatusCode.BadRequest,
            Problem.of(
                httpStatusCode = HttpStatusCode.BadRequest,
                problemDetail = String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"),
                code = ErrorCodes.BAD_REQUEST,
            ),
        )
    }
}

private fun Routing.getHistoriqueConnaissanceClientRoute(connaissanceClientService: ConnaissanceClientService) {
    get("/personnes/{idPersonne}/historique-connaissance-client") {
        call.parameters["idPersonne"]?.let { idPersonne ->
            val idPersonne = IdPersonne(idPersonne.toLong())
            call.respond(HttpStatusCode.OK, connaissanceClientService.getHistorique(call.tenantId(), idPersonne).toDto())
        } ?: call.respondProblem(
            HttpStatusCode.BadRequest,
            Problem.of(
                httpStatusCode = HttpStatusCode.BadRequest,
                problemDetail = String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"),
                code = ErrorCodes.BAD_REQUEST,
            ),
        )
    }
}

private fun Routing.createConnaissanceClientRoute(connaissanceClientService: ConnaissanceClientService) {
    post("/personnes/{idPersonne}/connaissance-client") {
        val connaissanceClientDto = call.receive<ConnaissanceClientDto>()

        call.parameters["idPersonne"]?.let { idPersonne ->
            val idPersonne = IdPersonne(idPersonne.toLong())
            val connaissanceClient = connaissanceClientDto.toDomain(idPersonne)
            val login = call.login()

            connaissanceClientService.sauvegarderEtHistoriserModification(
                call.tenantId(),
                connaissanceClient,
                TraceAudit(user = User(login), date = Instant.now(), TypeOperation.MODIFICATION),
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
                                HttpStatusCode.InternalServerError,
                                Problem.of(
                                    httpStatusCode = HttpStatusCode.InternalServerError,
                                    problemDetail = it.message,
                                    code = ErrorCodes.BAD_REQUEST,
                                ),
                            )
                    }
                }
        } ?: call.respondProblem(
            HttpStatusCode.BadRequest,
            Problem.of(
                httpStatusCode = HttpStatusCode.BadRequest,
                problemDetail = String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"),
                code = ErrorCodes.BAD_REQUEST,
            ),
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
                    call.respondProblem(
                        HttpStatusCode.InternalServerError,
                        Problem.of(
                            httpStatusCode = HttpStatusCode.InternalServerError,
                            problemDetail = it.message,
                            code = ErrorCodes.BAD_REQUEST,
                        ),
                    )
                }
        } ?: call.respondProblem(
            HttpStatusCode.BadRequest,
            Problem.of(
                httpStatusCode = HttpStatusCode.BadRequest,
                problemDetail = String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"),
                code = ErrorCodes.BAD_REQUEST,
            ),
        )
    }
}
