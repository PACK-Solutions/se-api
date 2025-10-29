package com.ps.personne.rest.connaissance.client

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.model.TypeOperation
import com.ps.personne.model.User
import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.rest.kyc.dto.request.ConnaissanceClientDto
import com.ps.personne.rest.kyc.dto.request.toDto
import com.ps.personne.rest.kyc.dto.response.toDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import java.time.Instant
import kotlin.text.toLong

private const val MESSAGE_HEADER_MANQUANT = "%s header manquant"
private const val MESSAGE_PARAMETRE_MANQUANT = "%s parameter manquant"

/**
 * Configure connaissance client check routes
 */
fun Application.configureConnaissanceClientRoutes(connaissanceClientService: ConnaissanceClientService) {
    routing {
        post("/personnes/{idPersonne}/connaissance-client") {
            val connaissanceClientDto = call.receive<ConnaissanceClientDto>()

            call.request.headers["login"]?.let { login ->
                call.parameters["idPersonne"]?.let { idPersonne ->
                    val idPersonne = IdPersonne(idPersonne.toLong())
                    val connaissanceClient = connaissanceClientDto.toDomain(idPersonne)

                    connaissanceClientService.sauvegarderEtHistoriserModification(
                        connaissanceClient,
                        TraceAudit(user = User(login), date = Instant.now(), TypeOperation.MODIFICATION)
                    )
                        .onSuccess { call.respond(HttpStatusCode.Created, connaissanceClientDto) }
                        .onFailure { call.respond(HttpStatusCode.InternalServerError, it.message) }
                } ?: call.respond(HttpStatusCode.BadRequest, String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"))
            } ?: call.respond(HttpStatusCode.BadRequest, String.format(MESSAGE_HEADER_MANQUANT, "login"))
        }

        put("/personnes/{idPersonne}/connaissance-client") {
            val connaissanceClientDto = call.receive<ConnaissanceClientDto>()

            call.request.headers["login"]?.let { login ->
                call.parameters["idPersonne"]?.let { idPersonne ->
                    val idPersonne = IdPersonne(idPersonne.toLong())
                    val connaissanceClient = connaissanceClientDto.toDomain(idPersonne)

                    connaissanceClientService.sauvegarderEtHistoriserModification(
                        // TODO : paramétrer la gestion des exceptions
                        connaissanceClient,
                        TraceAudit(user = User(login), date = Instant.now(), TypeOperation.CORRECTION)
                    )
                        .onSuccess { call.respond(HttpStatusCode.Created, connaissanceClientDto) }
                        .onFailure { call.respond(HttpStatusCode.InternalServerError, it.message) }
                } ?: call.respond(HttpStatusCode.BadRequest, String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"))
            } ?: call.respond(HttpStatusCode.BadRequest, String.format(MESSAGE_HEADER_MANQUANT, "login"))
        }

        get("/personnes/{idPersonne}/connaissance-client") {
            call.parameters["idPersonne"]?.let { idPersonne ->
                val idPersonne = IdPersonne(idPersonne.toLong())

                connaissanceClientService.getConnaissanceClient(idPersonne)
                    .onSuccess { connaissanceClient ->
                        connaissanceClient?.toDto()?.let {
                            call.respond(HttpStatusCode.OK, it)
                        }
                    }
                    .onFailure { call.respond(HttpStatusCode.InternalServerError, it.message) }
            } ?: call.respond(HttpStatusCode.BadRequest, String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"))
        }

        get("/personnes/{idPersonne}/historique-connaissance-client") {
            call.parameters["idPersonne"]?.let { idPersonne ->
                val idPersonne = IdPersonne(idPersonne.toLong())

                connaissanceClientService.getHistoriqueConnaissanceClient(idPersonne)
                    .onSuccess { historiqueModifications ->
                        historiqueModifications?.toDto()?.let {
                            call.respond(HttpStatusCode.OK, it)
                        }
                    }
                    .onFailure { call.respond(HttpStatusCode.InternalServerError, it.message) }
            } ?: call.respond(HttpStatusCode.BadRequest, String.format(MESSAGE_PARAMETRE_MANQUANT, "idPersonne"))
        }
    }
}
