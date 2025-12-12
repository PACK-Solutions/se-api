package com.ps.personne.config

import com.ps.personne.problem.ErrorCodes
import com.ps.personne.problem.respondProblem
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.statuspages.StatusPages

object ExceptionHandlingConfig {

    fun Application.configureExceptionHandling() {
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                call.application.log.error("Unhandled exception", cause)
                call.respondProblem(
                    status = HttpStatusCode.InternalServerError,
                    problemDetail = cause.message,
                    code = ErrorCodes.INTERNAL_SERVER_ERROR,
                )
            }
        }
    }
}
