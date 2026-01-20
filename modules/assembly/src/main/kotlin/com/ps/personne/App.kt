package com.ps.personne

import com.ps.kommand.CommandBus
import com.ps.kommand.CommandHandler
import com.ps.kommand.QueryBus
import com.ps.kommand.QueryHandler
import com.ps.kommand.event.EventBus
import com.ps.kommand.event.EventHandler
import com.ps.kommand.event.SynchronousEventBus
import com.ps.personne.config.ContextProviderConfig.configureContextProvider
import com.ps.personne.config.CorsConfig.configureCors
import com.ps.personne.config.ExceptionHandlingConfig.configureExceptionHandling
import com.ps.personne.config.InstancesConfig.configureInstances
import com.ps.personne.config.LoggingConfig.configureLogging
import com.ps.personne.config.SerializationConfig.configureSerialization
import com.ps.personne.config.SwaggerConfig.configureSwagger
import com.ps.personne.config.configureCommandBus
import com.ps.personne.config.configureQueryBus
import com.ps.personne.database.config.DatabaseConfig
import com.ps.personne.database.health.HealthCheckService
import com.ps.personne.database.repository.CoroutineContextTenantIdProvider
import com.ps.personne.database.repository.ExposedConnaissanceClientRepository
import com.ps.personne.database.repository.ExposedHistoriqueRepository
import com.ps.personne.database.repository.TenantIdProvider
import com.ps.personne.events.StoreHistoriqueOnAuditableEvent
import com.ps.personne.historique.EntreeHistoriqueIdGenerator
import com.ps.personne.historique.HistoriqueRepository
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.rest.config.MandatoryHeadersPlugin
import com.ps.personne.rest.routes.configureConnaissanceClientRoutes
import com.ps.personne.rest.routes.configureHealthRoutes
import com.ps.personne.rest.routes.configureHistoriqueRoutes
import com.ps.personne.usecases.EnregistrerConnnaissanceClientHandler
import com.ps.personne.usecases.RecupererConnnaissanceClientHandler
import com.ps.personne.usecases.RecupererHistoriqueConnnaissanceClientHandler
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.config.property
import io.ktor.server.netty.EngineMain
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.koinModule
import java.time.Clock

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.personne(moduleOverrides: Module.() -> Unit = {}) {
    configureCors()
    configureSwagger()
    configureSerialization()
    configureExceptionHandling()
    configureContextProvider()
    install(MandatoryHeadersPlugin)
    install(Koin)

    val sandbox = isSandbox()
    if (sandbox) {
        log.info("Sandbox mode enabled: skipping database configuration and migrations")
    } else {
        property<DatabaseConfig>("database").apply { configureDatabases() }
    }
    configureHealthRoutes(HealthCheckService(sandbox))
    configureLogging()

    configureInstances()
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

    configureConnaissanceClientRoutes(get<QueryBus>(), get<CommandBus>())
    configureHistoriqueRoutes(get<QueryBus>())
}

private fun Application.isSandbox(): Boolean = // todo utiliser Application.developmentMode ?
    environment.config.propertyOrNull("environment.sandbox")?.getString()?.toBoolean() ?: false
