package com.ps.personne.model

import java.time.LocalDate
import java.util.*

data class Mandat(val fonction: FonctionPPE, val dateFin: LocalDate?)

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
