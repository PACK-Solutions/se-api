import com.ps.personne.fixtures.PersonneFactory
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.model.User
import com.ps.personne.ports.driven.InMemoryHistoriqueExpositionRepository
import com.ps.personne.ports.driven.InMemoryPersonneRepository
import com.ps.personne.services.ExpositionPolitiqueServiceImpl
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.util.*

class ExpositionPolitiqueServiceTest : ExpectSpec(
    {

        // Use case 1: sauvegarder une nouvelle exposition politique
        // 0 : charger l'exposition politique courante
        // 1: clôturer en positionnant une date de fin sur le mandat (update)
        // 2: Sauvegarder une nouvelle exposition (insert)
        context("Sauvegarder une nouvelle exposition et cloturer la courante") {
            expect("Sauvegarder une nouvelle exposition") {
                val personneRepository = InMemoryPersonneRepository()
                val historiqueRepository = InMemoryHistoriqueExpositionRepository()
                val expositionPolitiqueService = ExpositionPolitiqueServiceImpl(
                    personneRepository,
                    historiqueRepository,
                )
                val idPersonne = IdPersonne(UUID.randomUUID())
                val nouvelleExpositionPolitique = PersonneFactory.creerExpositionPpe()
                val traceAudit = TraceAudit(
                    user = User("test"),
                    date = Instant.now(),
                )
                expositionPolitiqueService.sauverEtHistoriser(
                    idPersonne,
                    nouvelleExpositionPolitique,
                    traceAudit,
                )?.run {
                    val (personne, historique) = this
                    personne.expositionPolitique shouldBe nouvelleExpositionPolitique
                    historique.expositionPolitiques.size shouldBe 1
                    historique.expositionPolitiques.first() shouldBe nouvelleExpositionPolitique
                }
            }

            expect("Cloturer la courante") {
            }
        }
    },
)
