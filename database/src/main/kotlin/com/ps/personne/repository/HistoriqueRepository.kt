package com.ps.personne.repository

import com.ps.personne.historique.EntreeHistorique
import com.ps.personne.tables.HistoriqueTable
import org.jetbrains.exposed.sql.insert

interface HistoriqueRepository {
    fun store(entry: EntreeHistorique)
}

class ExposedHistoriqueRepository(val tenantIdProvider: TenantIdProvider) : HistoriqueRepository {
    override fun store(entry: EntreeHistorique) {
        HistoriqueTable.insert {
            it[id] = entry.id
            it[objectId] = entry.idObjet
            it[tenantId] = tenantIdProvider.tenantId()
            it[objectType] = entry.typeObjet
            it[changements] = entry.changements
            it[performedBy] = entry.performedBy
            it[occurredAt] = entry.occurredAt
        }
    }


}
