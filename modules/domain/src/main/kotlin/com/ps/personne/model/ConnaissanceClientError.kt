package com.ps.personne.model

import com.ps.framework.cqrs.bus.command.CommandError

sealed interface ConnaissanceClientError : CommandError {
    object VigilanceRenforceeObligatoire : ConnaissanceClientError
}
