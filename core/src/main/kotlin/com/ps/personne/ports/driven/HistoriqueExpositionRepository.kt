package com.ps.personne.ports.driven

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne

interface HistoriqueExpositionRepository {
    fun sauvegarder(expositionPolitique: ExpositionPolitique, idPersonne: IdPersonne)
    fun recupererHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique?
}
