package com.ps.personne.database.config

import com.ps.framework.cqrs.bus.Context
import com.ps.framework.cqrs.bus.command.Command
import com.ps.framework.cqrs.bus.command.CommandBusMiddleware
import com.ps.framework.cqrs.bus.command.CommandError
import com.ps.framework.cqrs.bus.command.CommandResult
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class CommandTransactionMiddleware(val next: CommandBusMiddleware) : CommandBusMiddleware {
    override suspend fun <C : Command<R, E>, R, E : CommandError> handle(command: C, context: Context): CommandResult<R, E> = newSuspendedTransaction {
        next.handle(command, context)
    }

    companion object {
        fun builder() = { next: CommandBusMiddleware? ->
            CommandTransactionMiddleware(
                next ?: error("next middleware is required for TransactionMiddleware"),
            )
        }
    }
}
