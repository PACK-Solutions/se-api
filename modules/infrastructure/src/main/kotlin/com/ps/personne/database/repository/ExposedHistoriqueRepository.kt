package com.ps.personne.database.repository

import com.ps.personne.database.model.ChangementDto
import com.ps.personne.database.tables.HistoriqueTable
import com.ps.personne.historique.EntreeHistorique
import com.ps.personne.historique.HistoriqueRepository
import org.jetbrains.exposed.sql.insert

class ExposedHistoriqueRepository(val tenantIdProvider: TenantIdProvider) : HistoriqueRepository {
    override fun store(entry: EntreeHistorique) {
        HistoriqueTable.insert {
            it[id] = entry.id
            it[objectId] = entry.idObjet
            it[tenantId] = tenantIdProvider.tenantId()
            it[objectType] = entry.typeObjet
            it[changements] = entry.changements.map(ChangementDto::from).toSet()
            it[performedBy] = entry.performedBy
            it[occurredAt] = entry.occurredAt
        }
    }
}
