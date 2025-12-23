package com.ps.personne.fixtures

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.LienParente
import com.ps.personne.model.Mandat
import java.time.LocalDate

data class PPEFixture(
    val fonction: FonctionPPE = FonctionPPE.DIRIGEANT_PARTI,
) {
    var dateFin: LocalDate? = null

    fun withDateFin(dateFin: LocalDate?): PPEFixture {
        this.dateFin = dateFin
        return this
    }

    fun build() = ExpositionPolitique.Ppe(Mandat(fonction, dateFin))
}

class ProchePPEFixture(
    val lienParente: LienParente = LienParente.PARENT,
    val fonction: FonctionPPE = FonctionPPE.DIRIGEANT_PARTI,
) {

    private var dateFin: LocalDate? = null

    fun withDateFin(dateFin: LocalDate?): ProchePPEFixture {
        this.dateFin = dateFin
        return this
    }

    fun build() = ExpositionPolitique.ProchePpe(lienParente, Mandat(fonction, dateFin))
}

fun aPPE(fonction: FonctionPPE) = PPEFixture(fonction)
fun aProchePPE(fonction: FonctionPPE, lienParente: LienParente) = ProchePPEFixture(lienParente, fonction)
