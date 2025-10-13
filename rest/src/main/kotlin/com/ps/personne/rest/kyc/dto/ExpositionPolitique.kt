package com.ps.personne.rest.kyc.dto

import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.IdExpositionPolitique
import com.ps.personne.model.LienParente
import com.ps.personne.model.MotifVigilance
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Mandat(val fonction: FonctionPPE, val dateFin: String?)

@Serializable
sealed interface ExpositionPolitique {
    val idExpositionPolitique: IdExpositionPolitique
    val dateDebut: LocalDate
    val vigilance: Vigilance

    class Ppe(
        override val idExpositionPolitique: IdExpositionPolitique,
        override val dateDebut: LocalDate,
        override val vigilance: AvecVigilanceRenforcee,
        val mandat: Mandat,
    ) : ExpositionPolitique

    class ProchePpe(
        override val idExpositionPolitique: IdExpositionPolitique,
        override val dateDebut: LocalDate,
        override val vigilance: Vigilance,
        val lienParente: LienParente,
        val mandat: Mandat,
    ) :
        ExpositionPolitique

    class Standard(
        override val idExpositionPolitique: IdExpositionPolitique,
        override val dateDebut: LocalDate,
        override val vigilance: Vigilance,
    ) : ExpositionPolitique
}

@JvmInline
value class IdExpositionPolitique(val uuid: UUID)

@Serializable
sealed interface Vigilance {
    val vigilanceRenforcee: Boolean
}

@Serializable
object SansVigilanceRenforcee : Vigilance {
    override val vigilanceRenforcee: Boolean = false
}

@Serializable
data class AvecVigilanceRenforcee(
    val motif: MotifVigilance,
) : Vigilance {
    override val vigilanceRenforcee: Boolean = true
}
