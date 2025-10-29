package com.ps.personne.ports.driven

import com.github.michaelbull.result.Result
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.model.HistoriqueModifications
import com.ps.personne.model.IdPersonne

interface ModificationsConnaissanceClientRepository {
    fun recupererHistorique(idPersonne: IdPersonne): Result<HistoriqueModifications, ConnaissanceClientError>
}
