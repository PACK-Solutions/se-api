package com.ps.personne.model

import java.time.LocalDate

sealed interface StatutPPE {
    val vigilance: Vigilance
    class PPE(val fonction: FonctionPPE, val dateFin: LocalDate?, override val vigilance: AvecVigilanceRenforcee) : StatutPPE
    class PROCHE_PPE(val lienParente: LienParente, ppe: PPE, override val vigilance: Vigilance) : StatutPPE
    class STANDARD(override val vigilance: Vigilance) : StatutPPE
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
