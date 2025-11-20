package com.ps.personne.rest.connaissance.client

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.model.TypeOperation
import com.ps.personne.model.User
import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.rest.kyc.dto.request.ConnaissanceClientDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.put
import java.time.Instant

fun Route.registerConnaissanceClientPutRoutes(connaissanceClientService: ConnaissanceClientService) {
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
}
