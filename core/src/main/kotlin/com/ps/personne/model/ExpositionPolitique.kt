package com.ps.personne.model

import java.time.Instant
import java.time.LocalDate
import java.util.*

data class Mandat(val fonction: FonctionPPE, val dateFin: LocalDate?)

sealed interface ExpositionPolitique {
    fun cloturer(date: Instant)

    val dateDebut: Instant
    val vigilance: Vigilance
    val dateCloture: Instant?

    data class Ppe(
        override val dateDebut: Instant,
        override val vigilance: AvecVigilanceRenforcee,
        override var dateCloture: Instant?,
        val mandat: Mandat,
    ) : ExpositionPolitique {
        override fun cloturer(date: Instant) {
            dateCloture = date
        }
    }

    data class ProchePpe(
        override val dateDebut: Instant,
        override val vigilance: Vigilance,
        override var dateCloture: Instant?,
        val lienParente: LienParente,
        val mandat: Mandat,
    ) : ExpositionPolitique {
        override fun cloturer(date: Instant) {
            dateCloture = date
        }
    }

    data class Standard(
        override val dateDebut: Instant,
        override val vigilance: Vigilance,
        override var dateCloture: Instant?,
    ) : ExpositionPolitique {
        override fun cloturer(date: Instant) {
            dateCloture = date
        }
    }
}

@JvmInline
value class IdExpositionPolitique(val uuid: UUID)

sealed interface Vigilance {
    val vigilanceRenforcee: Boolean
}

object SansVigilanceRenforcee : Vigilance {
    override val vigilanceRenforcee: Boolean = false
}

data class AvecVigilanceRenforcee(
    val motif: MotifVigilance,
) : Vigilance {
    override val vigilanceRenforcee: Boolean = true
}

enum class FonctionPPE {
    CHEF_ETAT,
    CHEF_GOUVERNEMENT,
    MEMBRE_GOUVERNEMENT,
    MEMBRE_PARLEMENT,
    DIRIGEANT_PARTI,
    MEMBRE_COUR_SUPREME,
    MEMBRE_COUR_COMPTES,
    DIRIGEANT_BANQUE_CENTRALE,
    AMBASSADEUR,
    OFFICIER_GENERAL,
    DIRIGEANT_ENTREPRISE_PUBLIQUE,
}

enum class LienParente {
    CONJOINT,
    ENFANT,
    CONJOINT_ENFANT,
    PARENT,
}

enum class MotifVigilance {
    AGE_AVANCE,
    DEMANDE_ASSUREUR,
    MONTANT_ELEVE,
    OPERATION_COMPLEXE,
    SANS_JUSTIFICATION,
}
