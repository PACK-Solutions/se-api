package com.ps.personne.config

import com.ps.kommand.CommandHandler
import com.ps.kommand.QueryHandler
import com.ps.kommand.event.EventHandler
import com.ps.personne.database.repository.CoroutineContextTenantIdProvider
import com.ps.personne.database.repository.ExposedConnaissanceClientRepository
import com.ps.personne.database.repository.ExposedHistoriqueRepository
import com.ps.personne.events.StoreHistoriqueOnAuditableEvent
import com.ps.personne.historique.HistoriqueRepository
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.InMemoryConnaissanceClientRepository
import com.ps.personne.ports.driven.ModificationsConnaissanceClientRepository
import com.ps.personne.usecases.EnregistrerConnnaissanceClientHandler
import com.ps.personne.usecases.RecupererConnnaissanceClientHandler
import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import kotlin.reflect.KClass

object InstancesConfig {
    // TODO à revoir pour faciliter l'utilisation par ailleurs
    val commandHandlersKey = AttributeKey<List<CommandHandler<*>>>("CommandHandlers")
    val repositoriesKey = AttributeKey<Map<KClass<*>, Any>>("Repositories")
    val queryHandlersKey = AttributeKey<List<QueryHandler<*>>>("QueryHandlers")
    val eventHandlersKey = AttributeKey<List<EventHandler<*>>>("EventHandlers")

    fun Application.configureInstances(sandBox: Boolean) {
        val tenantIdProvider = CoroutineContextTenantIdProvider()
        val connaissanceClientRepository = if (sandBox) {
            InMemoryConnaissanceClientRepository()
        } else {
            ExposedConnaissanceClientRepository(tenantIdProvider)
        }

        val historiqueRepository = ExposedHistoriqueRepository(tenantIdProvider)

        val repositories: Map<KClass<*>, Any> = mapOf(
            ConnaissanceClientRepository::class to connaissanceClientRepository,
            ModificationsConnaissanceClientRepository::class to connaissanceClientRepository,
            HistoriqueRepository::class to historiqueRepository,
        )

        val commandHandlers = listOf(
            EnregistrerConnnaissanceClientHandler(connaissanceClientRepository),
        )
        val queryHandlers = listOf(
            RecupererConnnaissanceClientHandler(connaissanceClientRepository),
        )

        val eventHandlers = listOf(
            StoreHistoriqueOnAuditableEvent(historiqueRepository),
        )

        this.attributes[commandHandlersKey] = commandHandlers
        this.attributes[queryHandlersKey] = queryHandlers
        this.attributes[repositoriesKey] = repositories
        this.attributes[eventHandlersKey] = eventHandlers
    }
}
