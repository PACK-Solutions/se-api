package com.ps.personne.ports.driven

import com.ps.personne.model.HistoriqueModifications
import com.ps.personne.model.IdPersonne

interface ModificationsConnaissanceClientRepository {
    fun recupererHistorique(tenantId: String, idPersonne: IdPersonne): HistoriqueModifications
}
