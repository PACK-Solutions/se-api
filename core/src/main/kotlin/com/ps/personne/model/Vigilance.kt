package com.ps.personne.model

sealed interface Vigilance {
    val vigilanceRenforcee: Boolean
}

object SansVigilanceRenforcee : Vigilance {
    override val vigilanceRenforcee: Boolean = false
}

data class AvecVigilanceRenforcee(
    val motifs: List<MotifVigilance>,
) : Vigilance {
    override val vigilanceRenforcee: Boolean = true
}

enum class MotifVigilance {
    AGE_AVANCE,
    DEMANDE_ASSUREUR,
    MONTANT_ELEVE,
    OPERATION_COMPLEXE,
    SANS_JUSTIFICATION,
}
