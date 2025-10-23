package com.ps.personne.fixtures

import com.ps.personne.model.*
import java.time.Instant

object ExpositionPolitiqueFactory {

    fun creerExpositionPpe() = ExpositionPolitique.Ppe(
        dateDebut = Instant.now(),
        vigilance = AvecVigilanceRenforcee(motif = MotifVigilance.DEMANDE_ASSUREUR),
        dateCloture = null,
        mandat = Mandat(FonctionPPE.DIRIGEANT_PARTI, null),
    )

    fun creerExpositionProchePpe() = ExpositionPolitique.ProchePpe(
        dateDebut = Instant.now(),
        vigilance = SansVigilanceRenforcee,
        dateCloture = null,
        lienParente = LienParente.PARENT,
        mandat = Mandat(FonctionPPE.DIRIGEANT_PARTI, null),
    )

}
