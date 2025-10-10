package com.ps.personne.model

import com.ps.personne.fixtures.DonneesKycFixtures
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

class HistoriqueKycTest : ExpectSpec(
    {
        context("Invariants de HistoriqueKyc: toutes les données doivent appartenir à la même personne") {
            expect("Cas valides: ensemble vide et ensembles avec uniquement le bon id") {
                val id = IdPersonne(100)

                // ensemble vide => OK (vacuous truth)
                HistoriqueKyc(idPersonne = id, donneesKyc = emptySet()).idPersonne shouldBe id

                // un seul élément correspondant => OK
                val k1 = DonneesKycFixtures.default(id = 100)
                HistoriqueKyc(idPersonne = id, donneesKyc = setOf(k1)).donneesKyc.size shouldBe 1

                // plusieurs éléments correspondant => OK
                val k2 = DonneesKycFixtures.standard(id = 100)
                val k3 = DonneesKycFixtures.prochePpe(id = 100)
                HistoriqueKyc(idPersonne = id, donneesKyc = setOf(k1, k2, k3)).donneesKyc.size shouldBe 3
            }

            context("Cas invalides: présence d'au moins un idPersonne différent") {
                data class Scenario(
                    val idHistorique: Long,
                    val donnees: Set<DonneesKyc>,
                    val idsAttendusDansMessage: Set<Long>,
                    val description: String,
                )

                // Données de test (data-driven)
                val scenarios = listOf(
                    Scenario(
                        idHistorique = 200,
                        donnees = setOf(
                            DonneesKycFixtures.default(id = 200),
                            DonneesKycFixtures.ppe(id = 201),
                        ),
                        idsAttendusDansMessage = setOf(201),
                        description = "un seul id différent",
                    ),
                    Scenario(
                        idHistorique = 300,
                        donnees = setOf(
                            DonneesKycFixtures.standard(id = 301),
                            DonneesKycFixtures.prochePpe(id = 302),
                        ),
                        idsAttendusDansMessage = setOf(301, 302),
                        description = "tous les ids sont différents de l'historique",
                    ),
                    Scenario(
                        idHistorique = 400,
                        donnees = setOf(
                            DonneesKycFixtures.default(id = 400),
                            DonneesKycFixtures.standard(id = 401),
                            DonneesKycFixtures.prochePpe(id = 401),
                            // id différent en double: doit être dédupliqué dans le message
                        ),
                        idsAttendusDansMessage = setOf(401),
                        description = "mêmes ids différents répétés: message doit contenir les ids distincts",
                    ),
                )

                scenarios.forEach { sc ->
                    expect(sc.description) {
                        val thrown = shouldThrow<IllegalArgumentException> {
                            HistoriqueKyc(idPersonne = IdPersonne(sc.idHistorique), donneesKyc = sc.donnees)
                        }
                        // Le message doit mentionner chaque id différent présent dans les données
                        sc.idsAttendusDansMessage.forEach { unexpectedId ->
                            thrown.message!! shouldBe thrown.message // non-null assertion + keep kotest happy
                        }
                        // Vérifications plus souples: chaque id doit apparaître dans le message
                        val message = thrown.message!!
                        sc.idsAttendusDansMessage.forEach { unexpectedId ->
                            message.contains(unexpectedId.toString()) shouldBe true
                        }
                    }
                }
            }
        }
    },
)
