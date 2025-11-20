package com.ps.personne.rest.connaissance.client

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.ps.personne.model.IdPersonne
import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.rest.kyc.dto.request.toDto
import com.ps.personne.rest.kyc.dto.response.toDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.registerConnaissanceClientGetRoutes(connaissanceClientService: ConnaissanceClientService) {
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
