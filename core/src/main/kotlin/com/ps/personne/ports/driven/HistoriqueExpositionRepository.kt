package com.ps.personne.ports.driven

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit

interface HistoriqueExpositionRepository {
    fun sauvegarder(expositionPolitique: ExpositionPolitique, idPersonne: IdPersonne)
    fun recuperer(idPersonne: IdPersonne): HistoriqueExpositionPolitique?
    fun sauvegarder(idPersonne: IdPersonne, expositionPolitique: ExpositionPolitique, traceAudit: TraceAudit) {}
}
