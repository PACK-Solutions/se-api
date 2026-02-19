package com.ps.personne.rest.problem

import com.ps.framework.cqrs.bus.command.CommandError
import com.ps.framework.cqrs.bus.query.QueryError
import com.ps.framework.ktor.problem.CommandErrorToProblemMapper
import com.ps.framework.ktor.problem.Problem
import com.ps.framework.ktor.problem.QueryErrorToProblemMapper
import com.ps.framework.ktor.problem.problem
import com.ps.personne.model.ConnaissanceClientError
import io.ktor.http.HttpStatusCode

object ConnaissanceClientCommandErrorToProblemMapper : CommandErrorToProblemMapper {
    override operator fun invoke(error: CommandError): Problem = when (error) {
        is ConnaissanceClientError -> error.toProblem()
        else -> problem(HttpStatusCode.InternalServerError) {}
    }
}

object ConnaissanceClientQueryErrorToProblemMapper : QueryErrorToProblemMapper {

    override operator fun invoke(error: QueryError): Problem = when (error) {
        is ConnaissanceClientError -> error.toProblem()
        else -> problem(HttpStatusCode.InternalServerError) {}
    }
}

fun ConnaissanceClientError.toProblem(): Problem = when (this) {
    ConnaissanceClientError.VigilanceRenforceeObligatoire -> problem(HttpStatusCode.UnprocessableEntity) {
        title = "La vigilance renforcée devrait être activée"
        detail = "La vigilance renforcée est obligatoire pour un PPE ou un proche PPE"
    }
}
