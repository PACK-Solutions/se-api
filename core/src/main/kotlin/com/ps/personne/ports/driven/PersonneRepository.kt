package com.ps.personne.ports.driven

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.Personne
import com.ps.personne.model.TraceAudit

interface PersonneRepository {
    fun sauvegarder(idPersonne: IdPersonne, expositionPolitique: ExpositionPolitique, traceAudit: TraceAudit): Personne
    fun recupererExpositionPolitiqueCourante(idPersonne: IdPersonne): ExpositionPolitique?
    //fun recupererHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique
}
