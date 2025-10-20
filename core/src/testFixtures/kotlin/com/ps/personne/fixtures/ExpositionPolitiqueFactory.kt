package com.ps.personne.fixtures

import com.ps.personne.model.AvecVigilanceRenforcee
import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.IdExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.LienParente
import com.ps.personne.model.Mandat
import com.ps.personne.model.MotifVigilance
import com.ps.personne.model.Personne
import com.ps.personne.model.SansVigilanceRenforcee
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

object ExpositionPolitiqueFactory {

    fun creerExpositionPpe() = ExpositionPolitique.Ppe(
        dateDebut = Instant.now(),
        vigilance = AvecVigilanceRenforcee(motif = MotifVigilance.DEMANDE_ASSUREUR),
        mandat = Mandat(FonctionPPE.DIRIGEANT_PARTI, null),
    )

    fun creerExpositionProchePpe() = ExpositionPolitique.ProchePpe(
        dateDebut = Instant.now(),
        vigilance = SansVigilanceRenforcee,
        lienParente = LienParente.PARENT,
        mandat = Mandat(FonctionPPE.DIRIGEANT_PARTI, null),
    )

    fun creerExpositionStandard() = ExpositionPolitique.Standard(
        dateDebut = Instant.now(),
        vigilance = SansVigilanceRenforcee,
    )

    fun creerPersonne(expositionPolitique: ExpositionPolitique) = Personne(
        idPersonne = IdPersonne(UUID.randomUUID()),
        expositionPolitique = expositionPolitique,
    )

}
