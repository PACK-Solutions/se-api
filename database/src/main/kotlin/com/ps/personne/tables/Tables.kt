package com.ps.personne.tables

import com.ps.personne.model.AvecVigilanceRenforceeDto
import com.ps.personne.model.ExpositionPolitiqueDto
import com.ps.personne.model.ModificationConnaissanceClientDto
import com.ps.personne.model.TypeOperationDto
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb

val format = Json { prettyPrint = true }

object ConnaissanceClientTable : Table("connaissance_client") {
    val personId = long("id_personne")
    val tenantId = varchar("tenant_id", length = 10)
    val statutPPE = jsonb<ExpositionPolitiqueDto.Ppe>(
        "statut_ppe",
        { value -> format.encodeToString(value) },
        { str -> format.decodeFromString(str) },
    ).nullable()
    val statutProchePPE = jsonb<ExpositionPolitiqueDto.ProchePpe>(
        "statut_proche_ppe",
        { value -> format.encodeToString(value) },
        { str -> format.decodeFromString(str) },
    ).nullable()
    val vigilance = jsonb<AvecVigilanceRenforceeDto>(
        "avec_vigilance_renforcee",
        { value -> format.encodeToString(value) },
        { str -> format.decodeFromString(str) },
    ).nullable()
}

@Suppress("MagicNumber")
object ConnaissanceClientHistoriqueTable : Table("connaissance_client_historique") {
    val id = uuid("id").uniqueIndex()

    val personId = long("id_personne")
    val tenantId = varchar("tenant_id", length = 50).default("default")
    val auditUser = varchar("audit_user", length = 100)
    val auditType = enumerationByName<TypeOperationDto>("audit_type", 30)
    val auditDate = varchar("audit_date", length = 100)
    val modifications = jsonb<Set<ModificationConnaissanceClientDto>>(
        "modifications",
        { value -> format.encodeToString(value) },
        { str -> format.decodeFromString(str) },
    )
}
