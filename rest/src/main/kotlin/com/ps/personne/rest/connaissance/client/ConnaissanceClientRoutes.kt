package com.ps.personne.rest.connaissance.client

import com.ps.personne.ports.driving.ConnaissanceClientService
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

/**
 * Configure connaissance client check routes
 */
fun Application.configureConnaissanceClientRoutes(connaissanceClientService: ConnaissanceClientService) {
    routing {
        registerConnaissanceClientPostRoutes(connaissanceClientService)
        registerConnaissanceClientPutRoutes(connaissanceClientService)
        registerConnaissanceClientGetRoutes(connaissanceClientService)
    }
}
