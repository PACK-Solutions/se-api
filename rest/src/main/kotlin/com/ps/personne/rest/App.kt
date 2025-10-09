package com.ps.personne.rest

import com.ps.personne.model.*
import com.ps.personne.model.DonneesKyc
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.UpdateInfo
import com.ps.personne.model.User
import com.ps.personne.rest.mappers.toDto
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate

fun main() {
    embeddedServer(Netty, port = 8000) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
        routing {
            get("/") {
                call.respond(prochePpe().toDto())
            }
        }
    }.start(wait = true)
}

fun prochePpe(
    id: Long = 3L,
    user: String = "proche_user",
    date: Instant = Instant.now(),
    lien: LienParente = LienParente.CONJOINT,
    ppeFonction: FonctionPPE = FonctionPPE.MEMBRE_PARLEMENT,
    ppeDateFin: LocalDate? = null,
    vigilanceRenforcee: Boolean = false,
): DonneesKyc = DonneesKyc(
    idPersonne = IdPersonne(id = id),
    update = UpdateInfo(User(login = user), date = date),
    statutPPE = StatutPPE.PROCHE_PPE(
        lienParente = lien,
        ppe = StatutPPE.PPE(
            fonction = ppeFonction,
            dateFin = ppeDateFin,
            vigilance = AvecVigilanceRenforcee(MotifVigilance.SANS_JUSTIFICATION),
        ),
        vigilance = if (vigilanceRenforcee) {
            AvecVigilanceRenforcee(
                MotifVigilance.DEMANDE_ASSUREUR,
            )
        } else {
            SansVigilanceRenforcee
        },
    ),
)
