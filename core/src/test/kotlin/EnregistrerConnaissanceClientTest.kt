import com.ps.kommand.BasicCommandBus
import com.ps.kommand.CommandResult
import com.ps.kommand.middleware.CommandDispatcherMiddleware
import com.ps.personne.fixtures.*
import com.ps.personne.model.*
import com.ps.personne.model.ConnaissanceClientEvent.ConnaissanceClientCreee
import com.ps.personne.model.ConnaissanceClientEvent.ConnaissanceClientModifiee
import com.ps.personne.ports.driven.ConnaissanceClientRepositoryError
import com.ps.personne.ports.driven.InMemoryConnaissanceClientRepository
import com.ps.personne.usecases.EnregistrerConnnaissanceClientCommand
import com.ps.personne.usecases.EnregistrerConnnaissanceClientHandler
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class EnregistrerConnaissanceClientTest : ShouldSpec(
    {
        val repository = InMemoryConnaissanceClientRepository()
        val commandHandler = EnregistrerConnnaissanceClientHandler(repository)

        val bus = BasicCommandBus(linkedSetOf(CommandDispatcherMiddleware.builder(listOf(commandHandler))))

        should("créer une connaissance client si elle n'existe pas") {
            val idPersonne = anIdPersonne()
            val command = EnregistrerConnnaissanceClientCommand(
                idPersonne,
                statutPPE = null,
                statutProchePPE = null,
                vigilance = AvecVigilanceRenforcee(emptyList()),
            )

            repository.recuperer(idPersonne).shouldBeFailure(ConnaissanceClientRepositoryError.PersonneNonTrouvee)

            val result = bus.dispatch(command, aContext().build())

            repository[idPersonne] shouldBe aConnaissanceClient(idPersonne)
                .withVigilanceRenforcee()
                .withoutStatutPPE()
                .withoutStatutProchePPE()
                .build()

            result shouldBe CommandResult.Success(idPersonne, listOf(ConnaissanceClientCreee(idPersonne)))
        }

        should("mettre à jour une connaissance client existante") {
            val idPersonne = anIdPersonne()
            val command = EnregistrerConnnaissanceClientCommand(
                idPersonne,
                statutPPE = null,
                statutProchePPE = aProchePPE(FonctionPPE.DIRIGEANT_PARTI, LienParente.PARENT).build(),
                vigilance = AvecVigilanceRenforcee(emptyList()),
            )

            repository.sauvegarder(aConnaissanceClient(idPersonne).build())

            val result = bus.dispatch(command, aContext().build())

            repository[idPersonne] shouldBe aConnaissanceClient(idPersonne)
                .withVigilanceRenforcee()
                .withoutStatutPPE()
                .withStatutProchePPE(LienParente.PARENT, FonctionPPE.DIRIGEANT_PARTI)
                .build()

            result shouldBe CommandResult.Success(idPersonne, listOf(ConnaissanceClientModifiee(idPersonne)))
        }

        should("ne pas enregistrer si la connaissance client est invalide et renvoyer une erreur") {
            val idPersonne = ConnaissanceClientFactory.creerIdPersonne()
            val invalidCommand = EnregistrerConnnaissanceClientCommand(
                idPersonne,
                statutPPE = aPPE(FonctionPPE.DIRIGEANT_PARTI).build(),
                statutProchePPE = aProchePPE(FonctionPPE.DIRIGEANT_PARTI, LienParente.PARENT).build(),
                vigilance = SansVigilanceRenforcee,
            )

            val result = bus.dispatch(invalidCommand, aContext().build())

            result shouldBe CommandResult.Failure(ConnaissanceClientError.VigilanceRenforceeObligatoire)
            repository[idPersonne] shouldBe null
        }
    },
)
