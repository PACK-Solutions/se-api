package com.ps.personne.database

import com.github.michaelbull.result.onSuccess
import com.ps.personne.fixtures.ConnaissanceClientFactory
import com.ps.personne.fixtures.TraceAuditFactory
import com.ps.personne.model.AjoutStatutPPE
import com.ps.personne.model.AjoutVigilance
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.SyntheseModifications
import com.ps.personne.repository.ExposedConnaissanceClientRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.postgresql.PostgreSQLContainer

class ConnaissanceClientRepositoryIT : BehaviorSpec(
    {
        val pg = PostgreSQLContainer("postgres:18")

        val tenandId = "pack"

        lateinit var ds: HikariDataSource

        beforeSpec {
            // Start container
            pg.start()

            // Build a Hikari datasource from the running container
            ds = HikariDataSource(
                HikariConfig().apply {
                    jdbcUrl = pg.jdbcUrl
                    username = pg.username
                    password = pg.password
                    driverClassName = "org.postgresql.Driver"
                },
            )

            // Connect Exposed and run Flyway migrations
            Database.connect(datasource = ds)
            val flyway =
                Flyway
                    .configure()
                    .dataSource(ds)
                    .baselineOnMigrate(true) // Used when migrating an existing database for the first time
                    .load()

            transaction {
                flyway.migrate()
            }
        }

        afterSpec {
            // Cleanup resources
            try {
                ds.close()
            } finally {
                pg.stop()
            }
        }

        context("Enregistrer une connaissance client en DB") {
            val repository = ExposedConnaissanceClientRepository()
            given("Une connaissance client") {
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClient()
                `when`("on enregistre la connaissance") {
                    val resultat = repository.sauvegarder(tenandId, connaissanceClient)
                    then("on recupère l'id de la personne") {
                        resultat shouldBe connaissanceClient.idPersonne
                    }
                }
            }
        }

        context("Récupérer une connaissance client en DB") {
            val repository = ExposedConnaissanceClientRepository()
            given("Une base de données vide") {
                val idPersonne = ConnaissanceClientFactory.creerIdPersonne()
                `when`("on lis la connaissance client") {
                    val resultat = repository.recuperer(tenandId, idPersonne)
                    then("on obtient une connaissance client vierge") {
                        resultat shouldBe ConnaissanceClient.vierge(idPersonne)
                    }
                }
            }
            given("Une connaissance client en DB") {
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClient()
                repository.sauvegarder(tenandId, connaissanceClient)
                `when`("on lis la connaissance client") {
                    val resultat = repository.recuperer(tenandId, connaissanceClient.idPersonne)
                    then("on obtient la connaissance client") {
                        resultat shouldBe connaissanceClient
                    }
                }
            }
        }

        context("Récupérer un historique de modification de connaissance client") {
            val repository = ExposedConnaissanceClientRepository()
            given("Une base de donnée vide") {
                val idPersonne = ConnaissanceClientFactory.creerIdPersonne()
                `when`("on récupère un historique") {
                    val resultat = repository.recupererHistorique(tenandId, idPersonne)
                    then("on obtient un historique vide") {
                        resultat.entreesHistorique.shouldBeEmpty()
                    }
                }
            }
            given("Une personne avec un historique de modification") {
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClient()
                val connaissanceClientModifiee = ConnaissanceClientFactory.creerConnaissanceClientPPE()
                val traceAudit = TraceAuditFactory.creerTraceAuditModification()
                connaissanceClient.appliquerModifications(
                    connaissanceClientModifiee,
                    traceAudit,
                ).onSuccess { repository.sauvegarder(tenandId, it) }
                `when`("on récupère son historique de modification") {
                    // TODO : Modifier factory pour passer l'id personne
                    val resultat = repository.recupererHistorique(tenandId, connaissanceClientModifiee.idPersonne)
                    then("on obtient un historique contenant la liste de ses modifications") {
                        resultat.entreesHistorique shouldContain SyntheseModifications(
                            traceAudit = traceAudit,
                            modifications = setOf(AjoutStatutPPE, AjoutVigilance),
                        )
                    }
                }
            }
        }
    },
)
