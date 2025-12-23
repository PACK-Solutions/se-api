package com.ps.personne.usecases

import aContext
import com.ps.kommand.BasicQueryBus
import com.ps.kommand.QueryResult
import com.ps.kommand.middleware.QueryDispatcherMiddleware
import com.ps.personne.fixtures.ConnaissanceClientFactory
import com.ps.personne.fixtures.aConnaissanceClient
import com.ps.personne.fixtures.anIdPersonne
import com.ps.personne.ports.driven.InMemoryConnaissanceClientRepository
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class RecupererConnaissanceClientTest : ShouldSpec(
    {
        val repository = InMemoryConnaissanceClientRepository()
        val queryHandler = RecupererConnnaissanceClientHandler(repository)

        val bus = BasicQueryBus(linkedSetOf(QueryDispatcherMiddleware.builder(listOf(queryHandler))))

        should("récuperer une connaissance client existante") {
            val idPersonne = anIdPersonne()
            val query = RecupererConnaissanceClientQuery(idPersonne)
            val connaissanceClient = aConnaissanceClient(idPersonne).withVigilanceRenforcee().build()

            repository.sauvegarder(connaissanceClient)

            val result = bus.dispatch(query, aContext().build())

            repository[idPersonne] shouldBe aConnaissanceClient(idPersonne)
                .withVigilanceRenforcee()
                .withoutStatutPPE()
                .withoutStatutProchePPE()
                .build()

            result shouldBe QueryResult.Success(connaissanceClient)
        }

        should("renvoyer la connaissance client par défaut si elle n'existe pas") {
            val idPersonne = ConnaissanceClientFactory.creerIdPersonne()
            val query = RecupererConnaissanceClientQuery(idPersonne)


            val result = bus.dispatch(query, aContext().build())

            result shouldBe QueryResult.Success(aConnaissanceClient(idPersonne).defaultValue())
        }

    },
)
