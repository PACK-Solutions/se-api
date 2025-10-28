package com.ps.personne.ports.driven

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ps.personne.model.ExpositionPolitiqueError
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne

class InMemoryHistoriqueExpositionPolitiqueRepository : HistoriqueExpositionPolitiqueRepository {

    private val historiqueExpositionPolitiques = mutableMapOf<IdPersonne, HistoriqueExpositionPolitique>()

    override fun recuperer(idPersonne: IdPersonne): Result<HistoriqueExpositionPolitique?, ExpositionPolitiqueError> {
        return Ok(historiqueExpositionPolitiques.getOrDefault(idPersonne, null))
    }

    override fun sauvegarder(historiqueExpositionPolitique: HistoriqueExpositionPolitique): Result<HistoriqueExpositionPolitique, ExpositionPolitiqueError> {
        historiqueExpositionPolitiques[historiqueExpositionPolitique.idPersonne] = historiqueExpositionPolitique
        return Ok(historiqueExpositionPolitique)
    }
}
