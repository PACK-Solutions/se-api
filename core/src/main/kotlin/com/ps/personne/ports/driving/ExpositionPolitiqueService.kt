package com.ps.personne.ports.driving

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.Personne
import com.ps.personne.model.TraceAudit

interface ExpositionPolitiqueService {
    fun sauverEtHistoriser(
        idPersonne: IdPersonne,
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): Pair<Personne, HistoriqueExpositionPolitique>?

    fun getHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique?


}
