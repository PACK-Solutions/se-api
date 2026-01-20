package com.ps.personne.config

import com.ps.kommand.CommandBus
import com.ps.kommand.CommandHandler
import com.ps.kommand.QueryBus
import com.ps.kommand.QueryHandler
import com.ps.kommand.event.EventBus
import com.ps.kommand.event.EventHandler
import com.ps.kommand.event.SynchronousEventBus
import com.ps.personne.RandomUUIDGenerator
import com.ps.personne.database.repository.CoroutineContextTenantIdProvider
import com.ps.personne.database.repository.ExposedConnaissanceClientRepository
import com.ps.personne.database.repository.ExposedHistoriqueRepository
import com.ps.personne.database.repository.TenantIdProvider
import com.ps.personne.events.StoreHistoriqueOnAuditableEvent
import com.ps.personne.historique.EntreeHistoriqueIdGenerator
import com.ps.personne.historique.HistoriqueRepository
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
            singleOf(::RandomUUIDGenerator) bind EntreeHistoriqueIdGenerator::class
            singleOf(::ExposedHistoriqueRepository) bind HistoriqueRepository::class
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
