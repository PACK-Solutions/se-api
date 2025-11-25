package com.ps.personne.repository

import com.ps.personne.model.*
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.ModificationsConnaissanceClientRepository
import com.ps.personne.tables.ConnaissanceClientHistoriqueTable
import com.ps.personne.tables.ConnaissanceClientHistoriqueTable.auditDate
import com.ps.personne.tables.ConnaissanceClientHistoriqueTable.auditType
import com.ps.personne.tables.ConnaissanceClientHistoriqueTable.auditUser
import com.ps.personne.tables.ConnaissanceClientHistoriqueTable.modifications
import com.ps.personne.tables.ConnaissanceClientTable
import com.ps.personne.tables.ConnaissanceClientTable.personId
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import java.util.*

class ExposedConnaissanceClientRepository : ConnaissanceClientRepository, ModificationsConnaissanceClientRepository {

    override fun recuperer(tenantId: String, idPersonne: IdPersonne): ConnaissanceClient {
        return transaction {
            ConnaissanceClientTable
                .selectAll()
                .where { (personId eq idPersonne.id) and (ConnaissanceClientTable.tenantId eq tenantId) }
                .singleOrNull()
                ?.let {
                    ConnaissanceClient(
                        idPersonne = idPersonne,
                        statutPPE = it[ConnaissanceClientTable.statutPPE]?.toDomain(),
                        statutProchePPE = it[ConnaissanceClientTable.statutProchePPE]?.toDomain(),
                        vigilance = it[ConnaissanceClientTable.vigilance].toDomain(),
                    )
                } ?: ConnaissanceClient.vierge(idPersonne)
        }
    }

    override fun sauvegarder(tenantId: String, connaissanceClient: ConnaissanceClient) = transaction {
        ConnaissanceClientTable
            .upsert(personId, ConnaissanceClientTable.tenantId) {
                it[personId] = connaissanceClient.idPersonne.id
                it[this.tenantId] = tenantId
                it[statutPPE] = connaissanceClient.statutPPE?.toDto()
                it[statutProchePPE] = connaissanceClient.statutProchePPE?.toDto()
                it[vigilance] = connaissanceClient.vigilance.toDto()
            }

        connaissanceClient.modification?.toDto()?.let { modificationSer ->
            ConnaissanceClientHistoriqueTable
                .insert {
                    it[id] = UUID.randomUUID()
                    it[personId] = connaissanceClient.idPersonne.id
                    it[ConnaissanceClientHistoriqueTable.tenantId] = tenantId
                    it[auditUser] = modificationSer.traceAudit.user
                    it[auditType] = modificationSer.traceAudit.typeOperation
                    it[auditDate] = modificationSer.traceAudit.date
                    it[modifications] = modificationSer.modifications
                }
        }

        connaissanceClient.idPersonne
    }

    override fun recupererHistorique(tenantId: String, idPersonne: IdPersonne) = transaction {
        var entreesHistorique = emptyList<SyntheseModifications>()
        ConnaissanceClientHistoriqueTable
            .selectAll()
            .where {
                (ConnaissanceClientHistoriqueTable.personId eq idPersonne.id) and
                    (ConnaissanceClientHistoriqueTable.tenantId eq tenantId)
            }
            .forEach {
                val syntheseModificationSer = SyntheseModificationDto(
                    traceAudit = TraceAuditDto(
                        user = it[auditUser],
                        typeOperation = it[auditType],
                        date = it[auditDate],
                    ),
                    modifications = it[modifications],
                )

                entreesHistorique = entreesHistorique.plus(syntheseModificationSer.toDomain())
            }
        HistoriqueModifications(idPersonne, entreesHistorique)
    }
}
