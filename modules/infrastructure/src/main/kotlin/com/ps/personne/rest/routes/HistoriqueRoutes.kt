package com.ps.personne.rest.routes

import com.ps.kommand.QueryBus
import com.ps.personne.database.historique.Changement
import com.ps.personne.database.historique.EntreeHistorique
import io.ktor.server.application.Application
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail
import kotlinx.serialization.Serializable

fun Application.configureHistoriqueRoutes(queryBus: QueryBus) {
    routing {
        get("/personnes/{idPersonne}/historique/connaissance-client", getHistoriqueConnaissanceClient(queryBus))

    }
}

@Serializable
data class EntreeHistoriqueDto(
    val idObjet: String,
    val changements: Set<Changement>,
    val performedBy: String,
    val occurredAt: String,
)

private fun EntreeHistorique.toDto() = EntreeHistoriqueDto(
    idObjet = this.idObjet,
    changements = this.changements,
    performedBy = this.performedBy,
    occurredAt = this.occurredAt.toString(),
)

private fun getHistoriqueConnaissanceClient(queryBus: QueryBus): suspend RoutingContext.() -> Unit = {
    val idPersonne = call.parameters.getOrFail<Long>("idPersonne")

//    val result = queryBus.dispatch(RecupererHistoriqueConnaissanceClientQuery) historiqueRepository.get("ConnaissanceClient", idPersonne.toString())
//        .sortedByDescending(EntreeHistorique::occurredAt)
//        .map(EntreeHistorique::toDto)
//    call.respond(result.toTypedArray())
}
