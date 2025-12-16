package com.ps.personne

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.ps.kommand.CommandResult
import com.ps.kommand.DomainEvent
import com.ps.kommand.QueryResult


fun <R, E> CommandResult<R, E>.toResult(): com.github.michaelbull.result.Result<Pair<R, List<DomainEvent>>, E> = when (this) {
    is CommandResult.Success<R> -> Ok(this.result to this.events)
    is CommandResult.Failure<E> -> Err(error)
}

fun <R, E> QueryResult<R, E>.toResult(): com.github.michaelbull.result.Result<R, E> = when (this) {
    is QueryResult.Success<R> -> Ok(this.result)
    is QueryResult.Failure<E> -> Err(error)
}
