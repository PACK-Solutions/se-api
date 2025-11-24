package com.ps.personne.model

import java.time.LocalDate

data class Mandat(val fonction: FonctionPPE, val dateFin: LocalDate?)

sealed interface ExpositionPolitique {

    data class Ppe(
        val mandat: Mandat,
    ) : ExpositionPolitique

    data class ProchePpe(
        val lienParente: LienParente,
        val mandat: Mandat,
    ) : ExpositionPolitique
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
