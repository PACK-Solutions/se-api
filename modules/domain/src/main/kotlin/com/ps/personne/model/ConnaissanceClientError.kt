package com.ps.personne.model

sealed interface ConnaissanceClientError : BusinessError {
    object VigilanceRenforceeObligatoire : ConnaissanceClientError
}
