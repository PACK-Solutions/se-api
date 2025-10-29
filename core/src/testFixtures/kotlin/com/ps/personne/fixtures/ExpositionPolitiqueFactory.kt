package com.ps.personne.fixtures

import com.ps.personne.model.*

object ExpositionPolitiqueFactory {

    fun creerExpositionPpe() = ExpositionPolitique.Ppe(
        mandat = Mandat(FonctionPPE.DIRIGEANT_PARTI, null),
    )

    fun creerExpositionProchePpe() = ExpositionPolitique.ProchePpe(
        lienParente = LienParente.PARENT,
        mandat = Mandat(FonctionPPE.DIRIGEANT_PARTI, null),
    )

}
