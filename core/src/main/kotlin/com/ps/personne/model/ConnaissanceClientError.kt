package com.ps.personne.model

sealed interface ConnaissanceClientError {

    val message: String

    data class AucuneModification(
        override val message: String,
    ) : ConnaissanceClientError

    data class VigilanceRenforceeObligatoire(
        override val message: String,
    ) : ConnaissanceClientError

    data class AucuneConnaissancePourIdPersonne(
        override val message: String,
        val idPersonne: IdPersonne,
    ) : ConnaissanceClientError
}
