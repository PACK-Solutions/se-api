package com.ps.personne.usecases

import com.ps.kommand.BasicQueryBus
import com.ps.kommand.QueryResult
import com.ps.kommand.middleware.QueryDispatcherMiddleware
import com.ps.personne.fixtures.anIdPersonne
import com.ps.personne.historique.*
import com.ps.personne.testharness.TestUUIDGenerator
import com.ps.personne.testharness.fixtures.aContext
import com.ps.personne.testharness.fixtures.aCreation
import com.ps.personne.testharness.fixtures.aModification
import com.ps.personne.testharness.fixtures.anEntreeHistorique
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeSortedBy
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.time.Instant
import java.time.temporal.ChronoUnit

class RecupererHistoriqueConnaissanceClientTest : ShouldSpec(
    {
        val typeObjet = TypeObjet("ConnaissanceClient")

        val uuidGenerator = TestUUIDGenerator()

        val repository = InMemoryHistoriqueRepository()
        val queryHandler = RecupererHistoriqueConnnaissanceClientHandler(repository)

        val bus = BasicQueryBus(linkedSetOf(QueryDispatcherMiddleware.builder(listOf(queryHandler))))

        should("renvoyer un historique vide si aucun historique n'existe") {
            val idPersonne = anIdPersonne()
            val result = bus.dispatch(RecupererHistoriqueConnaissanceClientQuery(idPersonne), aContext().build())

            result shouldBe QueryResult.Success(emptyList())
        }
        should("récuperer un historique de connaissance client existant") {
            val idPersonne = anIdPersonne()
            val idObjet = IdObjet(idPersonne.id.toString())
            val date = Instant.now()

            repository.store(
                anEntreeHistorique(typeObjet, idObjet)
                    .withID(uuidGenerator.next())
                    .withChangements(
                        aModification("vigilance", "false", "true"),
                        aCreation("fonction_ppe", "MEMBRE_PARLEMENT"),
                    )
                    .occuringAt(date)
                    .build(),
            )
            repository.store(
                anEntreeHistorique(typeObjet, idObjet)
                    .withID(uuidGenerator.next())
                    .withChangement(aCreation("motifs_vigilance", "[MONTANT_ELEVE, AGE_AVANCE]"))
                    .occuringAt(date.plus(5, ChronoUnit.MINUTES))
                    .build(),
            )

            val result = bus.dispatch(RecupererHistoriqueConnaissanceClientQuery(idPersonne), aContext().build())
            result.shouldBeTypeOf<QueryResult.Success<List<EntreeHistorique>>>()
            result.result shouldBe (
                listOf(
                    EntreeHistorique(
                        id = uuidGenerator[0],
                        typeObjet,
                        idObjet = IdObjet(idPersonne.id.toString()),
                        changements = setOf(
                            aCreation("fonction_ppe", "MEMBRE_PARLEMENT"),
                            aModification("vigilance", "false", "true"),
                        ),
                        performedBy = Author("unknown"),
                        occurredAt = date,
                    ),
                    EntreeHistorique(
                        id = uuidGenerator[1],
                        typeObjet,
                        idObjet = IdObjet(idPersonne.id.toString()),
                        changements = setOf(aCreation("motifs_vigilance", "[MONTANT_ELEVE, AGE_AVANCE]")),
                        performedBy = Author("unknown"),
                        occurredAt = date.plus(5, ChronoUnit.MINUTES),
                    ),
                )
                )
        }

        should(
            "ordonner les elements par date d'occurence croissante",
        ) { // TODO est-ce vraiment un tri croissant qu'on souhaite?
            val idPersonne = anIdPersonne()
            val idObjet = IdObjet(idPersonne.id.toString())
            val date = Instant.now()

            val template = anEntreeHistorique(typeObjet, idObjet)
                .withID(uuidGenerator.next())
                .withChangements(
                    aModification("vigilance", "false", "true"),
                )

            repository.store(template.occuringAt(date.plus(10, ChronoUnit.MINUTES)).build())
            repository.store(template.occuringAt(date).build())

            val result = bus.dispatch(RecupererHistoriqueConnaissanceClientQuery(idPersonne), aContext().build())
            result.shouldBeTypeOf<QueryResult.Success<List<EntreeHistorique>>>()
            result.result shouldBeSortedBy EntreeHistorique::occurredAt
        }
    },
)
