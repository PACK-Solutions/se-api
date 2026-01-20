package com.ps.personne.events

import com.ps.kommand.event.SynchronousEventBus
import com.ps.personne._testharness.fixtures.aContext
import com.ps.personne._testharness.fixtures.aModification
import com.ps.personne.fixtures.aConnaissanceClient
import com.ps.personne.fixtures.anIdPersonne
import com.ps.personne.fixtures.historique.InMemoryHistoriqueRepository
import com.ps.personne.historique.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.clock.TestClock
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.ZoneId
import java.util.*

class StoreHistoriqueOnConnaissanceClientTest : ShouldSpec(
    {
        val typeObjet = TypeObjet("ConnaissanceClient")

        val repository = InMemoryHistoriqueRepository()

        val generatedIds = mutableListOf<UUID>()
        val testUUIDGenerator = { UUID.randomUUID().also(generatedIds::add) }

        val testClock = TestClock(Instant.now(), ZoneId.systemDefault())

        val eventBus =
            SynchronousEventBus(listOf(StoreHistoriqueOnAuditableEvent(repository, testUUIDGenerator, testClock)))

        should("enregistrer une modification de connaissance client") {
            val idPersonne = anIdPersonne()
            val old = aConnaissanceClient(idPersonne).defaultValue()
            val new = aConnaissanceClient(idPersonne).withVigilanceRenforcee().build()

            eventBus.publish(ConnaissanceClientModifiee(old, new), aContext().build())

            repository.get(ConnaissanceClientHistoriqueProjection.typeObjet, IdObjet(idPersonne.id.toString())) shouldBe listOf(
                EntreeHistorique(
                    id = generatedIds[0],
                    typeObjet = ConnaissanceClientHistoriqueProjection.typeObjet,
                    idObjet = IdObjet(idPersonne.id.toString()),
                    changements = setOf(aModification("Vigilance", "false", "true")),
                    performedBy = Author("unknown"),
                    occurredAt = testClock.instant(),
                ),
            )
        }

        should("enregistrer l'auteur de la modification") {
            val idPersonne = anIdPersonne()
            val old = aConnaissanceClient(idPersonne).defaultValue()
            val new = aConnaissanceClient(idPersonne).withVigilanceRenforcee().build()

            eventBus.publish(ConnaissanceClientModifiee(old, new), aContext().withLogin("JeanMich").build())

            repository.get(ConnaissanceClientHistoriqueProjection.typeObjet, IdObjet(idPersonne.id.toString()))[0].performedBy shouldBe Author("JeanMich")
        }

    },
)
