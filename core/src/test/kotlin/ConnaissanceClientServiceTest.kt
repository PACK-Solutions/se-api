import com.ps.personne.fixtures.ConnaissanceClientFactory
import com.ps.personne.fixtures.TraceAuditFactory
import com.ps.personne.fixtures.shouldBeFailureOf
import com.ps.personne.fixtures.shouldBeSuccess
import com.ps.personne.model.AjoutStatutPPE
import com.ps.personne.model.AjoutVigilance
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.model.SyntheseModifications
import com.ps.personne.ports.driven.InMemoryConnaissanceClientRepository
import com.ps.personne.services.ConnaissanceClientServiceImpl
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class ConnaissanceClientServiceTest : BehaviorSpec(
    {
        context("Sauvegarder une connaissance client") {
            val connaissanceClientRepository = InMemoryConnaissanceClientRepository()
            val connaissanceClientService =
                ConnaissanceClientServiceImpl(connaissanceClientRepository, connaissanceClientRepository)

            given("Une personne en statut standard") {
                `when`("On lui ajoute un statut PPE avec vigilance renforcée sans motif") {
                    val traceAudit = TraceAuditFactory.creerTraceAuditModification()
                    val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClientPPE()
                    val resultat = connaissanceClientService.sauvegarderEtHistoriserModification(
                        "test",
                        connaissanceClient,
                        traceAudit,
                    )
                    then(
                        "On obtient l'id personne en retour",
                    ) {
                        resultat shouldBeSuccess { it shouldBe connaissanceClient.idPersonne }
                    }

                    val nouvelleConnaissanceClient = connaissanceClientService.getConnaissanceClient(
                        "test",
                        connaissanceClient.idPersonne,
                    )

                    then(
                        "On obtient une connaissance client avec le statut PPE et une vigilance renforcée",
                    ) {
                        nouvelleConnaissanceClient shouldBe connaissanceClient
                    }

                    then(
                        "On doit avoir les traces AjoutStatutPPE et AjoutVigilance dans l'historique de modifications",
                    ) {
                        connaissanceClientService.getHistorique(
                            "test",
                            connaissanceClient.idPersonne,
                        ).entreesHistorique.shouldContain(
                            SyntheseModifications(
                                traceAudit = traceAudit,
                                modifications = setOf(AjoutStatutPPE, AjoutVigilance),
                            ),
                        )
                    }
                }
            }
            `when`("On lui ajoute un statut proche PPE avec vigilance renforcée sans motif") {
                val traceAudit = TraceAuditFactory.creerTraceAuditModification()
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClientProchePPE()
                val resultat = connaissanceClientService.sauvegarderEtHistoriserModification(
                    "test",
                    connaissanceClient,
                    traceAudit,
                )
                then(
                    "On obtient l'id personne en retour",
                ) {
                    resultat shouldBeSuccess { it shouldBe connaissanceClient.idPersonne }
                }

                val nouvelleConnaissanceClient = connaissanceClientService.getConnaissanceClient(
                    "test",
                    connaissanceClient.idPersonne,
                )

                then(
                    "On obtient une connaissance client avec le statut proche PPE et une vigilance renforcée",
                ) {
                    nouvelleConnaissanceClient shouldBe connaissanceClient
                }
            }
            `when`("On lui ajoute une vigilance renforcée sans motif ni statut PPE ni proche PPE") {
                val idPersonne = ConnaissanceClientFactory.creerIdPersonne()
                val traceAudit = TraceAuditFactory.creerTraceAuditModification()
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClientVigilance()
                val resultat = connaissanceClientService.sauvegarderEtHistoriserModification(
                    "test",
                    connaissanceClient,
                    traceAudit,
                )

                then(
                    "On obtient l'id personne en retour",
                ) {
                    resultat shouldBeSuccess { it shouldBe connaissanceClient.idPersonne }
                }

                val nouvelleConnaissanceClient = connaissanceClientService.getConnaissanceClient(
                    "test",
                    connaissanceClient.idPersonne,
                )

                then(
                    "On obtient une connaissance avc une vigilance renforcée et pas d'exposition politique",
                ) {
                    nouvelleConnaissanceClient shouldBe connaissanceClient
                }
            }
            `when`("On lui ajoute un statut PPE sans vigilance renforcée") {
                val idPersonne = ConnaissanceClientFactory.creerIdPersonne()
                val traceAudit = TraceAuditFactory.creerTraceAuditModification()
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClientPPESansVigilance()
                val resultat = connaissanceClientService.sauvegarderEtHistoriserModification(
                    "test",
                    connaissanceClient,
                    traceAudit,
                )
                then("On obtient une erreur car la vigilance renforcée est obligatoire") {
                    resultat.shouldBeFailureOf<ConnaissanceClientError.VigilanceRenforceeObligatoire>()
                }
            }
            `when`("On lui ajoute un statut proche PPE sans vigilance renforcée") {
                val idPersonne = ConnaissanceClientFactory.creerIdPersonne()
                val traceAudit = TraceAuditFactory.creerTraceAuditModification()
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClientProchePPESansVigilance()
                val resultat = connaissanceClientService.sauvegarderEtHistoriserModification(
                    "test",
                    connaissanceClient,
                    traceAudit,
                )
                then("On obtient une erreur de type VigilanceRenforceeObligatoire") {
                    resultat.shouldBeFailureOf<ConnaissanceClientError.VigilanceRenforceeObligatoire>()
                }
            }

            given("Une personne avec une connaissance client complète") {
                val traceAudit = TraceAuditFactory.creerTraceAuditModification()
                val connaissanceClient = ConnaissanceClientFactory.creerConnaissanceClientComplete()
                connaissanceClientService.sauvegarderEtHistoriserModification(
                    "test",
                    connaissanceClient,
                    traceAudit,
                )

                `when`("On enregistre la même connaissance client") {
                    val traceAudit = TraceAuditFactory.creerTraceAuditModification()
                    val resultat = connaissanceClientService.sauvegarderEtHistoriserModification(
                        "test",
                        connaissanceClient,
                        traceAudit,
                    )
                    then("On obtient l'id personne en retour") {
                        resultat.shouldBeSuccess { it shouldBe connaissanceClient.idPersonne }
                    }
                }
            }
        }
    },
)
