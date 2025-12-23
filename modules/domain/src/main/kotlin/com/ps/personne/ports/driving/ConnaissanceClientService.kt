package com.ps.personne.ports.driving

import com.github.michaelbull.result.Result
import com.ps.personne.model.*

interface ConnaissanceClientService {
    fun getConnaissanceClient(tenantId: String, idPersonne: IdPersonne): ConnaissanceClient
    fun sauvegarderEtHistoriserModification(
        tenantId: String,
        connaissanceClient: ConnaissanceClient,
        traceAudit: TraceAudit,
    ): Result<IdPersonne, ConnaissanceClientError>

    fun getHistorique(tenantId: String, idPersonne: IdPersonne): HistoriqueModifications
}
