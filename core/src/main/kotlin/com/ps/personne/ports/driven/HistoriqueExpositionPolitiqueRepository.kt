package com.ps.personne.ports.driven

import com.github.michaelbull.result.Result
import com.ps.personne.model.ExpositionPolitiqueError
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne

interface HistoriqueExpositionPolitiqueRepository {
    fun recuperer(idPersonne: IdPersonne): Result<HistoriqueExpositionPolitique?, ExpositionPolitiqueError>
    fun sauvegarder(historiqueExpositionPolitique: HistoriqueExpositionPolitique): Result<HistoriqueExpositionPolitique, ExpositionPolitiqueError>
}
