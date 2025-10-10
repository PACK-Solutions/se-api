package com.ps.personne.model

import java.time.LocalDate

data class Mandat(val fonction: FonctionPPE, val dateFin: LocalDate?)

sealed interface StatutKyc {
    val dateDebut: LocalDate
    val vigilance: Vigilance
    class Ppe(val mandat: Mandat, override val dateDebut: LocalDate, override val vigilance: AvecVigilanceRenforcee) : StatutKyc
    class ProchePpe(val lienParente: LienParente, val mandat: Mandat, override val dateDebut: LocalDate, override val vigilance: Vigilance) : StatutKyc
    class Standard(override val dateDebut: LocalDate, override val vigilance: Vigilance) : StatutKyc
}

sealed interface Vigilance {
    val vigilanceRenforcee: Boolean
}

object SansVigilanceRenforcee : Vigilance {
    override val vigilanceRenforcee: Boolean = false
}

data class AvecVigilanceRenforcee(
    val motif: MotifVigilance
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
