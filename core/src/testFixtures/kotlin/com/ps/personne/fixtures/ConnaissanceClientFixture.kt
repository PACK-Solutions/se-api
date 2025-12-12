package com.ps.personne.fixtures

import com.ps.personne.model.*
import java.time.LocalDate
import kotlin.random.Random

class ConnaissanceClientFixture(val idPersonne: IdPersonne) {


    private var vigilance: Vigilance = SansVigilanceRenforcee
    private var statutProchePPE: ExpositionPolitique.ProchePpe? = null
    private var statutPPE: ExpositionPolitique.Ppe? = null

    fun withVigilanceRenforcee(vararg motifs: MotifVigilance): ConnaissanceClientFixture {
        vigilance = AvecVigilanceRenforcee(motifs.toList())
        return this
    }

    fun withoutVigilanceRenforcee(): ConnaissanceClientFixture {
        vigilance = SansVigilanceRenforcee
        return this
    }

    fun withStatutPPE(fonction: FonctionPPE, dateFin: LocalDate? = null): ConnaissanceClientFixture {
        statutPPE = ExpositionPolitique.Ppe(Mandat(fonction, dateFin))
        return this
    }

    fun withStatutProchePPE(lien: LienParente, fonction: FonctionPPE, dateFin: LocalDate? = null): ConnaissanceClientFixture {
        statutProchePPE = ExpositionPolitique.ProchePpe(lien, Mandat(fonction, dateFin))
        return this
    }

    fun build() = ConnaissanceClient(
        idPersonne,
        statutPPE,
        statutProchePPE,
        vigilance,
    )

    fun defaultValue() = ConnaissanceClient(
        idPersonne,
        null,
        null,
        SansVigilanceRenforcee,
    )

    fun withoutStatutPPE(): ConnaissanceClientFixture {
        statutPPE = null
        return this
    }

    fun withoutStatutProchePPE(): ConnaissanceClientFixture {
        statutProchePPE = null
        return this
    }
}


fun aConnaissanceClient(idPersonne: IdPersonne = IdPersonne(Random.nextLong())) = ConnaissanceClientFixture(idPersonne)
