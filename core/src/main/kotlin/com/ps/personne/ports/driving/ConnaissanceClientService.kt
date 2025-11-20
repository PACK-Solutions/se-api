package com.ps.personne.ports.driving

import com.github.michaelbull.result.Result
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.ConnaissanceClientError
import com.ps.personne.model.HistoriqueModifications
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit

interface ConnaissanceClientService {
    fun getConnaissanceClient(idPersonne: IdPersonne): ConnaissanceClient?
    fun sauvegarderEtHistoriserModification(
        connaissanceClient: ConnaissanceClient,
        traceAudit: TraceAudit
    ): Result<IdPersonne, ConnaissanceClientError>

    fun getHistorique(idPersonne: IdPersonne): Result<HistoriqueModifications?, ConnaissanceClientError>
}
