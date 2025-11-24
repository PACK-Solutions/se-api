package com.ps.personne.ports.driving

import com.github.michaelbull.result.Result
import com.ps.personne.model.*

interface ConnaissanceClientService {
    fun getConnaissanceClient(idPersonne: IdPersonne): ConnaissanceClient?
    fun sauvegarderEtHistoriserModification(
        connaissanceClient: ConnaissanceClient,
        traceAudit: TraceAudit,
    ): Result<IdPersonne, ConnaissanceClientError>

    fun getHistorique(idPersonne: IdPersonne): Result<HistoriqueModifications?, ConnaissanceClientError>
}
