package com.ps.personne.ports.driving

import com.github.michaelbull.result.Result
import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.ExpositionPolitiqueError
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit

interface ExpositionPolitiqueService {
    fun sauverEtHistoriser(
        idPersonne: IdPersonne,
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): Result<HistoriqueExpositionPolitique, ExpositionPolitiqueError>

    fun getHistorique(idPersonne: IdPersonne): Result<HistoriqueExpositionPolitique?, ExpositionPolitiqueError>
}
