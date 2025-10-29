package com.ps.personne.rest.exposition_politique

import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.model.TypeOperation
import com.ps.personne.model.User
import com.ps.personne.ports.driven.InMemoryHistoriqueExpositionPolitiqueRepository
import com.ps.personne.services.ExpositionPolitiqueServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.time.Instant
import java.util.*
import com.ps.personne.rest.kyc.dto.ExpositionPolitique as ExpositionPolitiqueDto

/**
 * Configure exposition politique check routes
 */
fun Application.configureExpositionPolitiqueRoutes() {
    routing {
        // GET for humans and monitoring systems expecting a JSON body
        post("/{idPersonne}/exposition-politique") {
            val expositionPolitiqueDto = call.receive<ExpositionPolitiqueDto>()

            call.parameters["idPersonne"]?.let { idPersonne ->
                val expositionPolitique = expositionPolitiqueDto.toDomain()

                val service = ExpositionPolitiqueServiceImpl(InMemoryHistoriqueExpositionPolitiqueRepository())
                val response = service.sauverEtHistoriser(
                    IdPersonne(UUID.randomUUID()),
                    expositionPolitique,
                    TraceAudit(user = User("toto"), date = Instant.now(), TypeOperation.AJOUT),
                )

                call.respond(HttpStatusCode.Created, expositionPolitiqueDto)
            } ?: call.respond(HttpStatusCode.BadRequest)
        }
    }
}
