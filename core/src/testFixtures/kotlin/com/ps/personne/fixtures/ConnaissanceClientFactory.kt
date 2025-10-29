package com.ps.personne.fixtures

import com.ps.personne.model.AvecVigilanceRenforcee
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.SansVigilanceRenforcee
import kotlin.random.Random

object ConnaissanceClientFactory {

    fun creerIdPersonne() = IdPersonne(Random.nextLong())

    fun creerConnaissanceClient() = ConnaissanceClient.vierge(creerIdPersonne())

    fun creerConnaissanceClientPPE() = ConnaissanceClient(
        idPersonne = creerIdPersonne(),
        statutPPE = ExpositionPolitiqueFactory.creerExpositionPpe(),
        statutProchePPE = null,
        vigilance = AvecVigilanceRenforcee(emptyList()),
    )

    fun creerConnaissanceClientProchePPE() = ConnaissanceClient(
        idPersonne = creerIdPersonne(),
        statutPPE = null,
        statutProchePPE = ExpositionPolitiqueFactory.creerExpositionProchePpe(),
        vigilance = AvecVigilanceRenforcee(emptyList()),
    )

    fun creerConnaissanceClientPPESansVigilance() = ConnaissanceClient(
        idPersonne = creerIdPersonne(),
        statutPPE = ExpositionPolitiqueFactory.creerExpositionPpe(),
        statutProchePPE = null,
        vigilance = SansVigilanceRenforcee,
    )

    fun creerConnaissanceClientProchePPESansVigilance() = ConnaissanceClient(
        idPersonne = creerIdPersonne(),
        statutPPE = null,
        statutProchePPE = ExpositionPolitiqueFactory.creerExpositionProchePpe(),
        vigilance = SansVigilanceRenforcee,
    )

    fun creerConnaissanceClientVigilance() = ConnaissanceClient(
        idPersonne = creerIdPersonne(),
        statutPPE = null,
        statutProchePPE = null,
        vigilance = AvecVigilanceRenforcee(emptyList()),
    )

    fun creerConnaissanceClientComplete() = ConnaissanceClient(
        idPersonne = creerIdPersonne(),
        statutPPE = ExpositionPolitiqueFactory.creerExpositionPpe(),
        statutProchePPE = ExpositionPolitiqueFactory.creerExpositionProchePpe(),
        vigilance = AvecVigilanceRenforcee(emptyList()),
    )
}
