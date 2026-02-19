package com.ps.personne.config

import com.ps.framework.cqrs.bus.command.BasicCommandBus
import com.ps.framework.cqrs.bus.command.CommandBus
import com.ps.framework.cqrs.bus.command.CommandHandler
import com.ps.framework.cqrs.bus.command.middleware.CommandDispatcherMiddleware
import com.ps.framework.cqrs.bus.command.middleware.EventDispatcherMiddleware
import com.ps.framework.cqrs.bus.event.EventBus
import com.ps.personne.database.config.CommandTransactionMiddleware

fun configureCommandBus(commandHandlers: List<CommandHandler<*>>, eventBus: EventBus): CommandBus = BasicCommandBus(
    linkedSetOf(
        CommandTransactionMiddleware.builder(),
        EventDispatcherMiddleware.builder(eventBus),
        CommandDispatcherMiddleware.builder(commandHandlers),
    ),
)
