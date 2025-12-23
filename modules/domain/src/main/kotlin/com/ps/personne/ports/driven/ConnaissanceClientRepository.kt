package com.ps.personne.ports.driven

import com.github.michaelbull.result.Result
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.IdPersonne

sealed interface ConnaissanceClientRepositoryError {
    object PersonneNonTrouvee : ConnaissanceClientRepositoryError
}

interface ConnaissanceClientRepository {
    fun recuperer(idPersonne: IdPersonne): Result<ConnaissanceClient, ConnaissanceClientRepositoryError>
    fun sauvegarder(connaissanceClient: ConnaissanceClient): IdPersonne
}
