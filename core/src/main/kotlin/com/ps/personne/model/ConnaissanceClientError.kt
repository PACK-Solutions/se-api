package com.ps.personne.model

sealed interface ConnaissanceClientError {

    object AucuneModification : ConnaissanceClientError

    object VigilanceRenforceeObligatoire : ConnaissanceClientError
}
