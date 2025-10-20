package com.ps.personne.fixtures

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.Personne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driving.ExpositionPolitiqueService

class FakeExpositionPolitiqueService : ExpositionPolitiqueService {
    override fun sauverEtHistoriser(
        idPersonne: IdPersonne,
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): HistoriqueExpositionPolitique? {
        TODO()
    }

    override fun getHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique {
        TODO("Not yet implemented")
    }
}
