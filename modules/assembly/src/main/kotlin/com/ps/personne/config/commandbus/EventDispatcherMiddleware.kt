package com.ps.personne.config.commandbus

import com.ps.kommand.Command
import com.ps.kommand.CommandBusMiddleware
import com.ps.kommand.CommandResult
import com.ps.kommand.Context
import com.ps.kommand.event.EventBus

class EventDispatcherMiddleware(val eventBus: EventBus, val next: CommandBusMiddleware) : CommandBusMiddleware {
    override suspend fun <C : Command<R, E>, R, E> handle(command: C, context: Context): CommandResult<R, E> {
        val result = next.handle(command, context)
        if (result is CommandResult.Success) {
            result.events.forEach { eventBus.publish(it, context) }
        }
        return result
    }

    companion object {
        fun builder(eventBus: EventBus) = { next: CommandBusMiddleware? ->
            EventDispatcherMiddleware(
                eventBus,
                next ?: error("next middleware is required for EventDispatcherMiddleware"),
            )
        }
    }
}
