package com.ps.personne.config

import com.ps.personne.problem.ErrorCodes
import com.ps.personne.problem.Problem
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
                "Unhandled technical error".also {
                    call.application.log.error(it, cause)
                    call.respondProblem(
                        HttpStatusCode.InternalServerError,
                        Problem.of(
                            httpStatusCode = HttpStatusCode.InternalServerError,
                            problemDetail = it,
                            code = ErrorCodes.INTERNAL_SERVER_ERROR,
                        ),
                    )
                }
            }
        }
    }
}
