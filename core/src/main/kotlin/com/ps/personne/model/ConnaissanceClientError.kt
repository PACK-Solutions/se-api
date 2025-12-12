package com.ps.personne.model

sealed interface ConnaissanceClientError {

    data class AucuneModification(
        val message: String,
    ) : ConnaissanceClientError

    object VigilanceRenforceeObligatoire : ConnaissanceClientError
}
