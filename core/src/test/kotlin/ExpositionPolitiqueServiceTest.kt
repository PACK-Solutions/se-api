import com.ps.personne.fixtures.FakePersonneRepository
import com.ps.personne.fixtures.PersonneFactory
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit
import com.ps.personne.model.User
import com.ps.personne.services.ExpositionPolitiqueServiceImpl
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.util.UUID

class ExpositionPolitiqueServiceTest : ExpectSpec(
    {

        context("Sauvegarder et historiser une exposition politique") {
            expect("Enregistrement avec succès") {
                val repository = FakePersonneRepository()
                val donneesKycService = ExpositionPolitiqueServiceImpl(repository)
                val idPersonne = IdPersonne(UUID.randomUUID())
                val expositionPolitique = PersonneFactory.creerExpositionPpe()
                val traceAudit = TraceAudit(
                    user = User("test"),
                    date = Instant.now(),
                )
                donneesKycService.sauverEtHistoriser(idPersonne, expositionPolitique, traceAudit) shouldBe true
            }
        }
    },
)
