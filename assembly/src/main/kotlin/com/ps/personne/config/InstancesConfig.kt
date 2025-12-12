package com.ps.personne.config

import com.ps.kommand.CommandHandler
import com.ps.kommand.QueryHandler
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.InMemoryConnaissanceClientRepository
import com.ps.personne.ports.driven.ModificationsConnaissanceClientRepository
import com.ps.personne.repository.CoroutineContextTenantIdProvider
import com.ps.personne.repository.ExposedConnaissanceClientRepository
import com.ps.personne.usecases.EnregistrerConnnaissanceClientHandler
import com.ps.personne.usecases.RecupererConnnaissanceClientHandler
import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import kotlin.reflect.KClass

object InstancesConfig {

    val commandHandlersKey = AttributeKey<List<CommandHandler<*>>>("CommandHandlers")
    val repositoriesKey = AttributeKey<Map<KClass<*>, Any>>("Repositories")
    val queryHandlersKey = AttributeKey<List<QueryHandler<*>>>("QueryHandlers")

    fun Application.configureInstances(sandBox: Boolean) {
        val connaissanceClientRepository = if (sandBox) {
            InMemoryConnaissanceClientRepository()
        } else {
            ExposedConnaissanceClientRepository(CoroutineContextTenantIdProvider())
        }

        val repositories = mapOf(
            ConnaissanceClientRepository::class to connaissanceClientRepository,
            ModificationsConnaissanceClientRepository::class to connaissanceClientRepository,
        )

        val commandHandlers = listOf(
            EnregistrerConnnaissanceClientHandler(connaissanceClientRepository),
        )
        val queryHandlers = listOf(
            RecupererConnnaissanceClientHandler(connaissanceClientRepository),
        )

        this.attributes[commandHandlersKey] = commandHandlers
        this.attributes[queryHandlersKey] = queryHandlers
        this.attributes[repositoriesKey] = repositories
    }
}
