package com.ps.personne.config

import com.ps.kommand.BasicCommandBus
import com.ps.kommand.CommandBus
import com.ps.kommand.CommandHandler
import com.ps.kommand.middleware.CommandDispatcherMiddleware

fun configureCommandBus(commandHandlers: List<CommandHandler<*>>): CommandBus {
    return BasicCommandBus(
        linkedSetOf(
            CommandDispatcherMiddleware.builder(commandHandlers),
        ),
    )
}
