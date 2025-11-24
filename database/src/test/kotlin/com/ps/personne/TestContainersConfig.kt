package com.ps.personne

import com.github.michaelbull.result.onSuccess
import com.ps.personne.fixtures.ConnaissanceClientFactory
import com.ps.personne.fixtures.TraceAuditFactory
import com.ps.personne.fixtures.shouldBeSuccess
import com.ps.personne.model.AjoutStatutPPE
import com.ps.personne.model.AjoutVigilance
import com.ps.personne.model.SyntheseModifications
import com.ps.personne.repository.ExposedConnaissanceClientRepository
import com.ps.personne.tables.ConnaissanceClientTable
import com.ps.personne.tables.HistoriqueModificationConnaissanceClientTable
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.postgresql.PostgreSQLContainer

class TestContainersConfig : BehaviorSpec() {
    val pg = PostgreSQLContainer("postgres:18")

    val ds = install(JdbcDatabaseContainerExtension(pg))

    val database = Database.connect(datasource = ds)

    val flyway: Flyway? =
        Flyway
            .configure()
            .dataSource(ds)
            .baselineOnMigrate(true) // Used when migrating an existing database for the first time
            .load()

    init {
        transaction(database) {
            flyway?.migrate()
        }
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(ConnaissanceClientTable, HistoriqueModificationConnaissanceClientTable)
        }
        context("Enregistrer une connaissance client en DB") {
            val repository = ExposedConnaissanceClientRepository()
            given("Une connaissance client") {
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClient()

                `when`("on enregistre la connaissance") {
                    val resultat = repository.sauvegarder(connaissanceClient)

                    then("on recupère l'id de la personne") {
                        resultat shouldBeSuccess {
                            it shouldBe connaissanceClient.idPersonne
                        }
                    }
                }
            }
        }
        context("Récupérer une connaissance client en DB") {
            val repository = ExposedConnaissanceClientRepository()
            given("Une base de données vide") {
                val idPersonne = ConnaissanceClientFactory.creerIdPersonne()
                `when`("on lis la connaissance client") {
                    val resultat = repository.recuperer(idPersonne)
                    then("on obtient un objet vide") {
                        resultat shouldBe null
                    }
                }
            }
            given("Une connaissance client en DB") {
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClient()
                repository.sauvegarder(connaissanceClient)
                `when`("on lis la connaissance client") {
                    val resultat = repository.recuperer(connaissanceClient.idPersonne)
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
                    val resultat = repository.recupererHistorique(idPersonne)
                    then("on obtient un historique vide") {
                        resultat shouldBeSuccess {
                            it.entreesHistorique shouldBe emptyList()
                        }
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
                ).onSuccess(repository::sauvegarder)
                `when`("on récupère son historique de modification") {
                    // TODO : Modifier factory pour passer l'id personne
                    val resultat = repository.recupererHistorique(connaissanceClientModifiee.idPersonne)
                    then("on obtient un historique contenant la liste de ses modifications") {
                        resultat shouldBeSuccess {
                            it.entreesHistorique shouldContain SyntheseModifications(
                                traceAudit = traceAudit,
                                modifications = setOf(AjoutStatutPPE, AjoutVigilance),
                            )
                        }
                    }
                }
            }
        }
    }
}
