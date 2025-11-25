package com.ps.personne.http

import com.ps.personne.problem.ErrorCodes
import com.ps.personne.problem.Problem
import com.ps.personne.problem.respondProblem
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createApplicationPlugin
import io.ktor.util.AttributeKey

val MandatoryHeadersPlugin = createApplicationPlugin(name = "MandatoryHeadersPlugin") {
    onCall { call ->
        val login = call.request.headers[HeaderNames.LOGIN]
        val tenantId = call.request.headers[HeaderNames.TENANT_ID]

        if (login.isNullOrBlank()) {
            call.respondProblem(
                HttpStatusCode.BadRequest,
                Problem.of(
                    httpStatusCode = HttpStatusCode.BadRequest,
                    problemDetail = "le header ${HeaderNames.LOGIN} est manquant",
                    code = ErrorCodes.BAD_REQUEST,
                ),
            )
            return@onCall
        }

        if (tenantId.isNullOrBlank()) {
            call.respondProblem(
                HttpStatusCode.BadRequest,
                Problem.of(
                    httpStatusCode = HttpStatusCode.BadRequest,
                    problemDetail = "le header ${HeaderNames.TENANT_ID} est manquant",
                    code = ErrorCodes.BAD_REQUEST,
                ),
            )
            return@onCall
        }

        call.attributes.put(LoginAttributeKey, login)
        call.attributes.put(TenantIdAttributeKey, tenantId)
    }
}

fun ApplicationCall.login(): String = attributes[LoginAttributeKey]
fun ApplicationCall.tenantId(): String = attributes[TenantIdAttributeKey]

object HeaderNames {
    const val LOGIN = "login"
    const val TENANT_ID = "tenantId"
}

val LoginAttributeKey = AttributeKey<String>(HeaderNames.LOGIN)
val TenantIdAttributeKey = AttributeKey<String>(HeaderNames.TENANT_ID)
