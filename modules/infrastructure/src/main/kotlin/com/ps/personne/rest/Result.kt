package com.ps.personne.rest

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ps.kommand.CommandResult
import com.ps.kommand.QueryResult
import com.ps.kommand.event.DomainEvent


fun <R, E> CommandResult<R, E>.toResult(): Result<Pair<R, List<DomainEvent>>, E> = when (this) {
    is CommandResult.Success<R> -> Ok(this.result to this.events)
    is CommandResult.Failure<E> -> Err(error)
}

fun <R, E> QueryResult<R, E>.toResult(): Result<R, E> = when (this) {
    is QueryResult.Success<R> -> Ok(this.result)
    is QueryResult.Failure<E> -> Err(error)
}
