package com.ps.personne.model


data class HistoriqueExpositionPolitique(
    val idPersonne: IdPersonne,
    val expositionPolitiques: Set<ExpositionPolitique>,
    val expositionCourante: ExpositionPolitique
)
