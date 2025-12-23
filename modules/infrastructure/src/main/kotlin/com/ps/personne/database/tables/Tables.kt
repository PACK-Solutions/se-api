package com.ps.personne.database.tables

import com.ps.personne.database.historique.Changement
import com.ps.personne.database.model.AvecVigilanceRenforceeDto
import com.ps.personne.database.model.ExpositionPolitiqueDto
import com.ps.personne.database.model.ModificationConnaissanceClientDto
import com.ps.personne.database.model.TypeOperationDto
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.json.jsonb

private val jsonConfig = Json { prettyPrint = true }

object ConnaissanceClientTable : Table("connaissance_client") {
    val personId = long("id_personne")
    val tenantId = varchar("tenant_id", length = 10)
    val statutPPE = jsonb<ExpositionPolitiqueDto.Ppe>("statut_ppe", jsonConfig).nullable()
    val statutProchePPE = jsonb<ExpositionPolitiqueDto.ProchePpe>("statut_proche_ppe", jsonConfig).nullable()
    val vigilance = jsonb<AvecVigilanceRenforceeDto>("avec_vigilance_renforcee", jsonConfig).nullable()
}

@Suppress("MagicNumber")
object ConnaissanceClientHistoriqueTable : Table("connaissance_client_historique") {
    val id = uuid("id").uniqueIndex()

    val personId = long("id_personne")
    val tenantId = varchar("tenant_id", length = 50).default("default")
    val auditUser = varchar("audit_user", length = 100)
    val auditType = enumerationByName<TypeOperationDto>("audit_type", 30)
    val auditDate = varchar("audit_date", length = 100)
    val modifications = jsonb<Set<ModificationConnaissanceClientDto>>("modifications", jsonConfig)
}

@Suppress("MagicNumber")
object HistoriqueTable : Table("historique") {
    val id = uuid("id").uniqueIndex()
    val tenantId = varchar("tenant_id", length = 50).default("default")

    val objectType = varchar("object_type", length = 50)
    val objectId = varchar("object_id", length = 50)
    val changements = jsonb<Set<Changement>>("changements", jsonConfig)
    val performedBy = varchar("performed_by", length = 100)
    val occurredAt = timestamp("occurred_at")

}
