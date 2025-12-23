package com.ps.personne.database.config

import com.ps.kommand.Command
import com.ps.kommand.CommandBusMiddleware
import com.ps.kommand.CommandResult
import com.ps.kommand.Context
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class CommandTransactionMiddleware(val next: CommandBusMiddleware) : CommandBusMiddleware {
    override suspend fun <C : Command<R, E>, R, E> handle(command: C, context: Context): CommandResult<R, E> {
        return newSuspendedTransaction {
            next.handle(command, context)
        }
    }

    companion object {
        fun builder() = { next: CommandBusMiddleware? ->
            CommandTransactionMiddleware(
                next ?: error("next middleware is required for TransactionMiddleware"),
            )
        }
    }

}
