import com.ps.personne.fixtures.ExpositionPolitiqueFactory
import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TypeOperation
import com.ps.personne.model.TraceAudit
import com.ps.personne.model.User
import com.ps.personne.ports.driven.InMemoryHistoriqueExpositionRepository
import com.ps.personne.services.ExpositionPolitiqueServiceImpl
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.should
import java.time.Instant
import java.time.LocalDate
import java.util.*

class ExpositionPolitiqueServiceTest : BehaviorSpec(
    {

        // Use case 1: sauvegarder une nouvelle exposition politique
        // 0 : charger l'exposition politique courante
        // 1: clôturer en positionnant une date de fin sur le mandat (update)
        // 2: Sauvegarder une nouvelle exposition (insert)
        context("Sauvegarder une nouvelle exposition") {

            val historiqueRepository = InMemoryHistoriqueExpositionRepository()
            val expositionPolitiqueService = ExpositionPolitiqueServiceImpl( historiqueRepository)

            given("Une personne sans exposition politique préalable") {
                val idPersonne = IdPersonne(UUID.randomUUID())

                `when`("on lui ajoute une exposition politique") {
                    val nouvelleExpositionPolitique = ExpositionPolitiqueFactory.creerExpositionPpe()
                    val  historique = expositionPolitiqueService.sauverEtHistoriser(
                        idPersonne,
                        nouvelleExpositionPolitique,
                        TraceAudit(
                            user = User("test"),
                            date = Instant.now(),
                            typeOperation = TypeOperation.AJOUT,
                        ),
                    )!!
                    then("elle doit être présente dans l'historique") {
                        historique.expositionPolitiques.shouldContainExactly(nouvelleExpositionPolitique)
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
                        historique.expositionPolitiques
                            .find { it == expositionProchePpe }
                        should { expositionPolitique ->
                            @Suppress("CAST_NEVER_SUCCEEDS")
                            (expositionPolitique as ExpositionPolitique.ProchePpe)
                                .let { LocalDate.now().equals(it.mandat.dateFin) }
                        }
                    }
                }
            }
        }
    },
)
