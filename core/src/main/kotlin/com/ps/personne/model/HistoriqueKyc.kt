package com.ps.personne.model

class HistoriqueKyc(
    val idPersonne: IdPersonne,
    val donneesKyc: Set<DonneesKyc>,
) {
    init {
        val idDivergents = donneesKyc.filter { it.idPersonne != idPersonne }.map { it.idPersonne }.distinct()
        require(
            idDivergents.isEmpty(),
        ) {
            "L'historique de la personne $idPersonne est incohérent et comporte des données kyc des personnes suivantes : ${idDivergents.joinToString()}"
        }
    }
}
