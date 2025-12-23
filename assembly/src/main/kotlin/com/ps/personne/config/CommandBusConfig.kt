package com.ps.personne.config

import com.ps.kommand.BasicCommandBus
import com.ps.kommand.CommandBus
import com.ps.kommand.CommandHandler
import com.ps.kommand.event.EventBus
import com.ps.kommand.middleware.CommandDispatcherMiddleware
import com.ps.personne.config.commandbus.EventDispatcherMiddleware

fun configureCommandBus(commandHandlers: List<CommandHandler<*>>, eventBus: EventBus): CommandBus {
    return BasicCommandBus(
        linkedSetOf(
            CommandTransactionMiddleware.builder(),
            EventDispatcherMiddleware.builder(eventBus),
            CommandDispatcherMiddleware.builder(commandHandlers),
        ),
    )
}
