package com.ps.personne.historique

import com.ps.framework.components.diff.New
import com.ps.framework.components.diff.Old
import com.ps.personne.fixtures.aConnaissanceClient
import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.LienParente
import com.ps.personne.model.MotifVigilance
import com.ps.personne.testharness.fixtures.aCreation
import com.ps.personne.testharness.fixtures.aModification
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class ConnaissanceClientHistoriqueProjectionTest :
    ShouldSpec(
        {

            should("record modifications on vigilance status") {
                ConnaissanceClientHistoryProjection(
                    Old(aConnaissanceClient().defaultValue()),
                    New(aConnaissanceClient().withVigilanceRenforcee().build()),
                ).getChangements() shouldBe setOf(
                    aModification("Vigilance", "false", "true"),
                )
            }

            should("record modifications on statut PPE") {
                ConnaissanceClientHistoryProjection(
                    Old(aConnaissanceClient().defaultValue()),
                    New(aConnaissanceClient().withStatutPPE(FonctionPPE.DIRIGEANT_PARTI).build()),
                ).getChangements() shouldBe setOf(
                    aCreation("FonctionPPE", "DIRIGEANT_PARTI"),
                )
            }

            should("record modifications on proche PPE") {
                ConnaissanceClientHistoryProjection(
                    Old(aConnaissanceClient().defaultValue()),
                    New(
                        aConnaissanceClient().withStatutProchePPE(
                            LienParente.CONJOINT,
                            FonctionPPE.DIRIGEANT_PARTI,
                        ).build(),
                    ),
                ).getChangements() shouldBe setOf(
                    aCreation("FonctionProchePPE", "DIRIGEANT_PARTI"),
                    aCreation("LienParenteProchePPE", "CONJOINT"),
                )
            }

            should("record modifications on fonction_ppe end date") {
                val endDate = LocalDate.of(2028, 1, 1)

                ConnaissanceClientHistoryProjection(
                    Old(aConnaissanceClient().withStatutPPE(FonctionPPE.DIRIGEANT_PARTI).build()),
                    New(aConnaissanceClient().withStatutPPE(FonctionPPE.DIRIGEANT_PARTI, endDate).build()),
                ).getChangements() shouldBe setOf(
                    aCreation("DateFinFonctionPPE", "2028-01-01"),
                )
            }

            should("record modifications on fonction proche ppe end date") {

                val previousDate = LocalDate.of(2028, 1, 1)
                val newDate = LocalDate.of(2029, 1, 1)
                ConnaissanceClientHistoryProjection(
                    Old(
                        aConnaissanceClient().withStatutProchePPE(
                            LienParente.CONJOINT,
                            FonctionPPE.DIRIGEANT_PARTI,
                            previousDate,
                        ).build(),
                    ),
                    New(
                        aConnaissanceClient().withStatutProchePPE(
                            LienParente.CONJOINT,
                            FonctionPPE.DIRIGEANT_PARTI,
                            newDate,
                        ).build(),
                    ),
                ).getChangements() shouldContain
                    aModification("DateFinFonctionProchePPE", "2028-01-01", "2029-01-01")
            }

            should("record modifications on motifs vigilance") {
                ConnaissanceClientHistoryProjection(
                    Old(aConnaissanceClient().withVigilanceRenforcee().build()),
                    New(
                        aConnaissanceClient().withVigilanceRenforcee(
                            MotifVigilance.MONTANT_ELEVE,
                            MotifVigilance.AGE_AVANCE,
                        ).build(),
                    ),
                ).getChangements() shouldContain
                    aCreation("MotifVigilance", "[MONTANT_ELEVE, AGE_AVANCE]")
            }

            should("not record modifications if nothing changed") {
                val connaissanceClient = aConnaissanceClient()
                    .withVigilanceRenforcee()
                    .withStatutPPE(FonctionPPE.DIRIGEANT_PARTI)
                    .withStatutProchePPE(LienParente.CONJOINT, FonctionPPE.DIRIGEANT_PARTI)
                    .build()

                ConnaissanceClientHistoryProjection(
                    Old(connaissanceClient),
                    New(connaissanceClient),
                ).getChangements() shouldBe emptySet()
            }
        },
    )
