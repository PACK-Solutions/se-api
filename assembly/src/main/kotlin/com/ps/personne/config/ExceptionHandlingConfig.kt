package com.ps.personne.config

import com.ps.personne.problem.ErrorCodes
import com.ps.personne.problem.respondProblem
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.MissingRequestParameterException
import io.ktor.server.plugins.ParameterConversionException
import io.ktor.server.plugins.statuspages.StatusPages

object ExceptionHandlingConfig {

    fun Application.configureExceptionHandling() {
        install(StatusPages) {

            exception<Throwable> { call, cause ->
                when (cause) {
                    is ParameterConversionException,
                    is MissingRequestParameterException,
                    -> call.respondProblem(
                        status = HttpStatusCode.BadRequest,
                        problemDetail = cause.message,
                        code = ErrorCodes.BAD_REQUEST,
                    )

                    else -> {
                        call.application.log.error("Unexpected error", cause)
                        call.respondProblem(
                            status = HttpStatusCode.InternalServerError,
                            problemDetail = cause.message,
                            code = ErrorCodes.INTERNAL_SERVER_ERROR,
                        )
                    }
                }

            }
        }
    }
}
