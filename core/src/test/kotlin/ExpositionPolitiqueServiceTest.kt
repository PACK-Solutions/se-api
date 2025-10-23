import com.ps.personne.fixtures.ExpositionPolitiqueFactory
import com.ps.personne.model.*
import com.ps.personne.ports.driven.InMemoryHistoriqueExpositionPolitiqueRepository
import com.ps.personne.services.ExpositionPolitiqueServiceImpl
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.time.Instant
import java.util.*

class ExpositionPolitiqueServiceTest : BehaviorSpec(
    {

        // Use case 1: sauvegarder une nouvelle exposition politique
        // 0 : charger l'exposition politique courante
        // 1: clôturer en positionnant une date de fin sur le mandat (update)
        // 2: Sauvegarder une nouvelle exposition (insert)
        context("Sauvegarder une nouvelle exposition") {

            val historiqueRepository = InMemoryHistoriqueExpositionPolitiqueRepository()
            val expositionPolitiqueService = ExpositionPolitiqueServiceImpl(historiqueRepository)

            given("Une personne sans exposition politique préalable") {
                val idPersonne = IdPersonne(UUID.randomUUID())

                `when`("on lui ajoute une exposition politique") {
                    val nouvelleExpositionPolitique = ExpositionPolitiqueFactory.creerExpositionPpe()
                    val traceAudit = TraceAudit(
                        user = User("test"),
                        date = Instant.now(),
                        typeOperation = TypeOperation.AJOUT,
                    )
                    val historique = expositionPolitiqueService.sauverEtHistoriser(
                        idPersonne,
                        nouvelleExpositionPolitique,
                        traceAudit,
                    )!!
                    then("elle doit être présente dans l'historique") {
                        historique.shouldContainExactly(
                            EntreeHistoriqueExpositionPolitique(nouvelleExpositionPolitique, traceAudit),
                        )
                    }
                    then("elle doit être l'exposition politique courante") {
                        historique.expositionCourante shouldBe nouvelleExpositionPolitique
                    }
                }
            }
            given("Une personne avec exposition politique préalable") {
                val idPersonne = IdPersonne(UUID.randomUUID())
                val expositionProchePpe = ExpositionPolitiqueFactory.creerExpositionProchePpe()
                expositionPolitiqueService.sauverEtHistoriser(
                    idPersonne,
                    expositionProchePpe,
                    TraceAudit(
                        user = User("test2"),
                        date = Instant.now(),
                        TypeOperation.AJOUT,
                    ),
                )!!

                `when`("on lui ajoute une exposition politique") {
                    val expositionPolitiquePpe = ExpositionPolitiqueFactory.creerExpositionPpe()
                    val historique = expositionPolitiqueService.sauverEtHistoriser(
                        idPersonne,
                        expositionPolitiquePpe,
                        TraceAudit(
                            user = User("test2"),
                            date = Instant.now(),
                            TypeOperation.AJOUT,
                        ),
                    )!!
                    then("l'exposition politique précédente doit être cloturée dans l'historique") {
                        historique
                            .find { it.expositionPolitique == expositionProchePpe }
                            ?.expositionPolitique
                            .shouldBeInstanceOf<ExpositionPolitique.ProchePpe> {
                                it.dateCloture.shouldNotBeNull()
                            }
                    }
                }
            }
        }
    },
)
