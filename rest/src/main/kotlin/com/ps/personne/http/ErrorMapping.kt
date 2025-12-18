package com.ps.personne.http

import com.ps.personne.model.BusinessError
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.problem.ErrorCode
import io.ktor.http.HttpStatusCode

class BusinessException(error: BusinessError) : RuntimeException() {
    override val message: String? = message(error)
    val httpCode = httpCode(error)
    val errorCode = errorCode(error)
}


private fun httpCode(error: BusinessError): HttpStatusCode = when (error) {
    is ConnaissanceClientError.VigilanceRenforceeObligatoire -> HttpStatusCode.BadRequest
}

private fun errorCode(error: BusinessError): ErrorCode = when (error) {
    is ConnaissanceClientError.VigilanceRenforceeObligatoire -> ErrorCode("CC_VRO")
}

private fun message(error: BusinessError): String? = when (error) {
    is ConnaissanceClientError.VigilanceRenforceeObligatoire -> "La vigilance renforcée est obligatoire pour un PPE ou un proche PPE"
}


