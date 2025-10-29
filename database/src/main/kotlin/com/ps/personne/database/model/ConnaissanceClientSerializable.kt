package com.ps.personne.database.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --------- Serializables used for JSONB persistence ---------

@Serializable
data class MandatSer(
    val fonction: FonctionPpeSer,
    // ISO-8601 date (yyyy-MM-dd) or null
    val dateFin: String?
)

@Serializable
sealed interface ExpositionPolitiqueSer {
    @Serializable
    @SerialName("ppe")
    data class Ppe(val mandat: MandatSer) : ExpositionPolitiqueSer

    @Serializable
    @SerialName("proche_ppe")
    data class ProchePpe(
        val lienParente: LienParenteSer,
        val mandat: MandatSer,
    ) : ExpositionPolitiqueSer
}

@Serializable
enum class FonctionPpeSer {
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

@Serializable
enum class LienParenteSer {
    CONJOINT,
    ENFANT,
    CONJOINT_ENFANT,
    PARENT,
}

@Serializable
data class AvecVigilanceRenforceeSer(val motifs: List<MotifVigilanceSer>)

@Serializable
enum class MotifVigilanceSer {
    AGE_AVANCE,
    DEMANDE_ASSUREUR,
    MONTANT_ELEVE,
    OPERATION_COMPLEXE,
    SANS_JUSTIFICATION,
}
