package com.ps.personne.database.mapper

import com.ps.personne.database.model.AvecVigilanceRenforceeSer
import com.ps.personne.database.model.ExpositionPolitiqueSer
import com.ps.personne.database.model.FonctionPpeSer
import com.ps.personne.database.model.LienParenteSer
import com.ps.personne.database.model.MandatSer
import com.ps.personne.database.model.MotifVigilanceSer
import com.ps.personne.model.AvecVigilanceRenforcee
import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.LienParente
import com.ps.personne.model.Mandat
import com.ps.personne.model.MotifVigilance
import com.ps.personne.model.SansVigilanceRenforcee
import com.ps.personne.model.Vigilance
import java.time.LocalDate

private fun LocalDate?.toIsoString(): String? = this?.toString()

internal fun Mandat.toSer(): MandatSer = MandatSer(
    fonction = FonctionPpeSer.valueOf(this.fonction.name),
    dateFin = this.dateFin.toIsoString(),
)

fun ExpositionPolitique.Ppe.toSer(): ExpositionPolitiqueSer.Ppe = ExpositionPolitiqueSer.Ppe(
    mandat = this.mandat.toSer()
)

fun ExpositionPolitique.ProchePpe.toSer(): ExpositionPolitiqueSer.ProchePpe = ExpositionPolitiqueSer.ProchePpe(
    lienParente = LienParenteSer.valueOf(this.lienParente.name),
    mandat = this.mandat.toSer(),
)

fun Vigilance.toSer(): AvecVigilanceRenforceeSer? = when (this) {
    is SansVigilanceRenforcee -> null
    is AvecVigilanceRenforcee -> AvecVigilanceRenforceeSer(this.motifs.map { MotifVigilanceSer.valueOf(it.name) })
}

fun MandatSer.toDomain(): Mandat = Mandat(
    fonction = FonctionPPE.valueOf(this.fonction.name),
    dateFin = this.dateFin?.let { LocalDate.parse(it) }
)

fun ExpositionPolitiqueSer.Ppe.toDomain(): ExpositionPolitique.Ppe = ExpositionPolitique.Ppe(
    mandat = this.mandat.toDomain()
)

fun ExpositionPolitiqueSer.ProchePpe.toDomain(): ExpositionPolitique.ProchePpe = ExpositionPolitique.ProchePpe(
    lienParente = LienParente.valueOf(this.lienParente.name),
    mandat = this.mandat.toDomain()
)

fun AvecVigilanceRenforceeSer?.toDomain(): Vigilance = when (this) {
    is AvecVigilanceRenforceeSer -> AvecVigilanceRenforcee(this.motifs.map { MotifVigilance.valueOf(it.name) })
    null -> SansVigilanceRenforcee
}
