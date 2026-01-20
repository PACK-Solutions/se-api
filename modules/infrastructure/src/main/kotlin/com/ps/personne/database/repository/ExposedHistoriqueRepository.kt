package com.ps.personne.database.repository

import com.ps.personne.database.model.ChangementDto
import com.ps.personne.database.tables.HistoriqueTable
import com.ps.personne.database.tables.HistoriqueTable.changements
import com.ps.personne.database.tables.HistoriqueTable.id
import com.ps.personne.database.tables.HistoriqueTable.objectId
import com.ps.personne.database.tables.HistoriqueTable.objectType
import com.ps.personne.database.tables.HistoriqueTable.occurredAt
import com.ps.personne.database.tables.HistoriqueTable.performedBy
import com.ps.personne.database.tables.HistoriqueTable.tenantId
import com.ps.personne.historique.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class ExposedHistoriqueRepository(val tenantIdProvider: TenantIdProvider) : HistoriqueRepository {
    override fun store(entry: EntreeHistorique) {
        HistoriqueTable.insert {
            it[id] = entry.id
            it[objectId] = entry.idObjet.value
            it[tenantId] = tenantIdProvider.tenantId()
            it[objectType] = entry.typeObjet.value
            it[changements] = entry.changements.map(ChangementDto::from).toSet()
            it[performedBy] = entry.performedBy.value
            it[occurredAt] = entry.occurredAt
        }
    }

    override fun get(
        typeObjet: TypeObjet,
        idObjet: IdObjet,
    ): List<EntreeHistorique> {
        return HistoriqueTable.selectAll().where {
            (objectType eq typeObjet.value) and
                (objectId eq idObjet.value) and
                (tenantId eq tenantIdProvider.tenantId())
        }.map {
            EntreeHistorique(
                id = it[id],
                typeObjet = TypeObjet(it[objectType]),
                idObjet = IdObjet(it[objectId]),
                changements = it[changements].map(ChangementDto::toDiff).toSet(),
                performedBy = Author(it[performedBy]),
                occurredAt = it[occurredAt],
            )
        }
    }
}
