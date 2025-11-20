package com.ps.personne.ports.driven

import com.github.michaelbull.result.Result
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.model.IdPersonne

interface ConnaissanceClientRepository {
    fun recuperer(idPersonne: IdPersonne): ConnaissanceClient?
    fun sauvegarder(connaissanceClient: ConnaissanceClient): Result<IdPersonne, ConnaissanceClientError>
}
