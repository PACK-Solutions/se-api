package com.ps.personne.config

import com.ps.framework.components.history.HistoryEventRepository
import com.ps.framework.components.id.IdGenerator
import com.ps.framework.components.id.RandomUUIDGenerator
import com.ps.framework.cqrs.bus.command.CommandBus
import com.ps.framework.cqrs.bus.command.CommandHandler
import com.ps.framework.cqrs.bus.event.EventBus
import com.ps.framework.cqrs.bus.event.EventHandler
import com.ps.framework.cqrs.bus.event.SynchronousEventBus
import com.ps.framework.cqrs.bus.query.QueryBus
import com.ps.framework.cqrs.bus.query.QueryHandler
import com.ps.framework.cqrs.multitenancy.CoroutineContextTenantIdProvider
import com.ps.framework.cqrs.multitenancy.TenantIdProvider
import com.ps.personne.database.repository.ExposedConnaissanceClientRepository
import com.ps.personne.database.repository.ExposedHistoryEventRepository
import com.ps.personne.events.StoreHistoriqueOnAuditableEvent
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.usecases.EnregistrerConnnaissanceClientHandler
import com.ps.personne.usecases.RecupererConnnaissanceClientHandler
import com.ps.personne.usecases.RecupererHistoriqueConnnaissanceClientHandler
import io.ktor.server.application.Application
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.ktor.plugin.koinModule
import java.time.Clock

object InstancesConfig {

    fun Application.configureKoin(moduleOverrides: Module.() -> Unit) {
        koinModule {
            singleOf(::CoroutineContextTenantIdProvider) bind TenantIdProvider::class
            singleOf(::RandomUUIDGenerator) bind IdGenerator::class
            singleOf(::ExposedHistoryEventRepository) bind HistoryEventRepository::class
            singleOf(::ExposedConnaissanceClientRepository) bind ConnaissanceClientRepository::class

            singleOf(::EnregistrerConnnaissanceClientHandler) bind CommandHandler::class

            singleOf(::RecupererConnnaissanceClientHandler) bind QueryHandler::class
            singleOf(::RecupererHistoriqueConnnaissanceClientHandler) bind QueryHandler::class

            singleOf(::StoreHistoriqueOnAuditableEvent) bind EventHandler::class

            singleOf(Clock::systemUTC) bind Clock::class

            single<EventBus> { SynchronousEventBus(getAll(EventHandler::class)) }
            single<CommandBus> { configureCommandBus(getAll(CommandHandler::class), get()) }
            single<QueryBus> { configureQueryBus(getAll(QueryHandler::class)) }
            this.apply(moduleOverrides)
        }
    }
}
