package com.ps.personne.ports.driven

import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.IdPersonne

interface ConnaissanceClientRepository {
    fun recuperer(tenantId: String, idPersonne: IdPersonne): ConnaissanceClient?
    fun sauvegarder(tenantId: String, connaissanceClient: ConnaissanceClient): IdPersonne
}
