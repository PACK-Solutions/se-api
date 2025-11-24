package com.ps.personne.tables

import com.ps.personne.model.AvecVigilanceRenforceeSer
import com.ps.personne.model.ExpositionPolitiqueSer
import com.ps.personne.model.ModificationConnaissanceClientSer
import com.ps.personne.model.TypeOperationSer
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.or

val format = Json { prettyPrint = true }

// Note: Besoin de rajouter un tenant ID (si jamais une seule DB pour l'ensemble des clients)
object ConnaissanceClientTable : Table("connaissance_client") {
    val personId = long("id_personne").uniqueIndex()
    val statutPPE = jsonb<ExpositionPolitiqueSer.Ppe>(
        "statut_ppe",
        { value -> format.encodeToString(value) },
        { str -> format.decodeFromString(str) },
    ).nullable()
    val statutProchePPE = jsonb<ExpositionPolitiqueSer.ProchePpe>(
        "statut_proche_ppe",
        { value -> format.encodeToString(value) },
        { str -> format.decodeFromString(str) },
    ).nullable()
    val vigilance = jsonb<AvecVigilanceRenforceeSer>(
        "avec_vigilance_renforcee",
        { value -> format.encodeToString(value) },
        { str -> format.decodeFromString(str) },
    ).nullable()

    init {
        check("vigilance_required_for_ppe_and_proche_ppe") {
            ((statutPPE.isNull()) and (statutProchePPE.isNull())) or (vigilance.isNotNull())
        }
    }
}

@Suppress("MagicNumber")
object ConnaissanceClientHistoriqueTable : Table("connaissance_client_historique") {
    val id = uuid("id").uniqueIndex()
    val personId = reference(
        name = "id_personne",
        refColumn = ConnaissanceClientTable.personId,
        fkName = "fk_connaissance_client_historique_id_personne",
    )
    val auditUser = varchar("audit_user", length = 100)
    val auditType = enumerationByName<TypeOperationSer>("audit_type", 30)
    val auditDate = varchar("audit_date", length = 100)
    val modifications = jsonb<Set<ModificationConnaissanceClientSer>>(
        "modifications",
        { value -> format.encodeToString(value) },
        { str -> format.decodeFromString(str) },
    )
}
