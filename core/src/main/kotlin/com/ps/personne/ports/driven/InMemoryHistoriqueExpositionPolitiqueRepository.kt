package com.ps.personne.ports.driven

import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne

class InMemoryHistoriqueExpositionPolitiqueRepository : HistoriqueExpositionPolitiqueRepository {

    private val historiqueExpositionPolitiques = mutableMapOf<IdPersonne, HistoriqueExpositionPolitique>()

    override fun recuperer(idPersonne: IdPersonne): HistoriqueExpositionPolitique? {
        return historiqueExpositionPolitiques.getOrDefault(idPersonne, null)
    }

    override fun sauvegarder(historiqueExpositionPolitique: HistoriqueExpositionPolitique): HistoriqueExpositionPolitique {
        historiqueExpositionPolitiques[historiqueExpositionPolitique.idPersonne] = historiqueExpositionPolitique
        return historiqueExpositionPolitique
    }
}
