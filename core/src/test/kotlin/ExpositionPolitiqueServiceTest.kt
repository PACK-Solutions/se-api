import com.ps.personne.fixtures.PersonneFactory
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.model.User
import com.ps.personne.ports.driven.InMemoryHistoriqueExpositionRepository
import com.ps.personne.ports.driven.InMemoryPersonneRepository
import com.ps.personne.services.ExpositionPolitiqueServiceImpl
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.util.*

class ExpositionPolitiqueServiceTest : BehaviorSpec(
    {

        // Use case 1: sauvegarder une nouvelle exposition politique
        // 0 : charger l'exposition politique courante
        // 1: clôturer en positionnant une date de fin sur le mandat (update)
        // 2: Sauvegarder une nouvelle exposition (insert)
        context("Sauvegarder une nouvelle exposition") {
            val personneRepository = InMemoryPersonneRepository()
            val historiqueRepository = InMemoryHistoriqueExpositionRepository()
            val expositionPolitiqueService = ExpositionPolitiqueServiceImpl(personneRepository, historiqueRepository)
            given("Une personne sans exposition politique préalable") {
                val idPersonne = IdPersonne(UUID.randomUUID())
                `when`("on lui ajoute une exposition politique") {
                    val nouvelleExpositionPolitique = PersonneFactory.creerExpositionPpe()
                    val traceAudit = TraceAudit(
                        user = User("test"),
                        date = Instant.now(),
                    )
                    val (personne, historique) = expositionPolitiqueService.sauverEtHistoriser(
                        idPersonne,
                        nouvelleExpositionPolitique,
                        traceAudit,
                    )!!
                    then("elle doit être l'exposition courante") {
                        personne.expositionPolitique shouldBe nouvelleExpositionPolitique
                    }
                    then("elle doit être présente dans l'historique") {
                        historique.expositionPolitiques.shouldContainExactly(nouvelleExpositionPolitique)
                    }
                }
            }
        }
    },
)
