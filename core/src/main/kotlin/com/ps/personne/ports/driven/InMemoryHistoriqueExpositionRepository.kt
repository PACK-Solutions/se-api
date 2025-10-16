package com.ps.personne.ports.driven

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne

class InMemoryHistoriqueExpositionRepository : HistoriqueExpositionRepository {

    private val historiqueExpositionPolitiques = mutableMapOf<IdPersonne, HistoriqueExpositionPolitique>()

    override fun sauvegarder(expositionPolitique: ExpositionPolitique, idPersonne: IdPersonne) {
        historiqueExpositionPolitiques.putIfAbsent(
            idPersonne,
            HistoriqueExpositionPolitique(idPersonne, setOf(expositionPolitique)),
        )
    }

    override fun recupererHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique? {
        return historiqueExpositionPolitiques.getValue(idPersonne)
    }
}
