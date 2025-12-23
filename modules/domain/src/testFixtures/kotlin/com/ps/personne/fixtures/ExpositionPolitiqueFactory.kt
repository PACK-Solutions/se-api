package com.ps.personne.fixtures

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.LienParente
import com.ps.personne.model.Mandat

object ExpositionPolitiqueFactory {

    fun creerExpositionPpe() = ExpositionPolitique.Ppe(
        mandat = Mandat(FonctionPPE.DIRIGEANT_PARTI, null),
    )

    fun creerExpositionProchePpe() = ExpositionPolitique.ProchePpe(
        lienParente = LienParente.PARENT,
        mandat = Mandat(FonctionPPE.DIRIGEANT_PARTI, null),
    )

}
