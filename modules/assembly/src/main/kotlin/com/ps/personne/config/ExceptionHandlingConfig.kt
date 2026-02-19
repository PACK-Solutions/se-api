package com.ps.personne.config

import com.ps.framework.ktor.configuration.psDefaults
import com.ps.personne.rest.problem.ConnaissanceClientCommandErrorToProblemMapper
import com.ps.personne.rest.problem.ConnaissanceClientQueryErrorToProblemMapper
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages

object ExceptionHandlingConfig {

    fun Application.configureExceptionHandling() {
        install(StatusPages) {
            psDefaults(
                ConnaissanceClientCommandErrorToProblemMapper,
                ConnaissanceClientQueryErrorToProblemMapper,
            )
        }
    }
}
