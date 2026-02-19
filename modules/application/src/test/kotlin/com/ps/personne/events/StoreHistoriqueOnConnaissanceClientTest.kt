package com.ps.personne.events

import com.ps.framework.components.history.Author
import com.ps.framework.components.history.HistoryEvent
import com.ps.framework.components.history.IdObjet
import com.ps.framework.cqrs.bus.event.SynchronousEventBus
import com.ps.personne.fixtures.aConnaissanceClient
import com.ps.personne.fixtures.anIdPersonne
import com.ps.personne.historique.ConnaissanceClientHistoryProjection
import com.ps.personne.historique.InMemoryHistoryEventRepository
import com.ps.personne.testharness.fixtures.aContext
import com.ps.personne.testharness.fixtures.aModification
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.clock.TestClock
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.ZoneId
import java.util.*

class StoreHistoriqueOnConnaissanceClientTest :
    ShouldSpec(
        {
            val repository = InMemoryHistoryEventRepository()

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

                repository.get(
                    ConnaissanceClientHistoryProjection.typeObjet,
                    IdObjet(idPersonne.id.toString()),
                ) shouldBe listOf(
                    HistoryEvent(
                        id = generatedIds[0],
                        typeObjet = ConnaissanceClientHistoryProjection.typeObjet,
                        idObjet = IdObjet(idPersonne.id.toString()),
                        diffs = setOf(aModification("Vigilance", "false", "true")),
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

                repository.get(
                    ConnaissanceClientHistoryProjection.typeObjet,
                    IdObjet(idPersonne.id.toString()),
                )[0].performedBy shouldBe Author("JeanMich")
            }
        },
    )
