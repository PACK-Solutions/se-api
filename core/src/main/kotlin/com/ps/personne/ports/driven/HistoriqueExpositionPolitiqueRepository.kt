package com.ps.personne.ports.driven

import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne

interface HistoriqueExpositionPolitiqueRepository {
    fun recuperer(idPersonne: IdPersonne): HistoriqueExpositionPolitique?
    fun sauvegarder(historiqueExpositionPolitique: HistoriqueExpositionPolitique): HistoriqueExpositionPolitique
}
