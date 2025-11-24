package com.ps.personne.repository

import com.ps.personne.mapper.toDomain
import com.ps.personne.mapper.toSer
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
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import java.util.*

class ExposedConnaissanceClientRepository : ConnaissanceClientRepository, ModificationsConnaissanceClientRepository {

    override fun recuperer(idPersonne: IdPersonne): ConnaissanceClient? {
        return transaction {
            ConnaissanceClientTable.selectAll().where { personId eq idPersonne.id }
                .singleOrNull()
                ?.let {
                    ConnaissanceClient(
                        idPersonne = idPersonne,
                        statutPPE = it[ConnaissanceClientTable.statutPPE]?.toDomain(),
                        statutProchePPE = it[ConnaissanceClientTable.statutProchePPE]?.toDomain(),
                        vigilance = it[ConnaissanceClientTable.vigilance].toDomain(),
                    )
                }
        }
    }

    override fun sauvegarder(connaissanceClient: ConnaissanceClient) = transaction {
        ConnaissanceClientTable.upsert(personId) {
            it[personId] = connaissanceClient.idPersonne.id
            it[statutPPE] = connaissanceClient.statutPPE?.toSer()
            it[statutProchePPE] = connaissanceClient.statutProchePPE?.toSer()
            it[vigilance] = connaissanceClient.vigilance.toSer()
        }

        connaissanceClient.modification?.toSer()?.let { modificationSer ->
            ConnaissanceClientHistoriqueTable.insert {
                it[id] = UUID.randomUUID()
                it[personId] = connaissanceClient.idPersonne.id
                it[auditUser] = modificationSer.traceAudit.user
                it[auditType] = modificationSer.traceAudit.typeOperation
                it[auditDate] = modificationSer.traceAudit.date
                it[modifications] = modificationSer.modifications
            }
        }

        connaissanceClient.idPersonne
    }

    override fun recupererHistorique(idPersonne: IdPersonne) = transaction {
        var entreesHistorique = emptyList<SyntheseModifications>()
        ConnaissanceClientHistoriqueTable.selectAll().where {
            ConnaissanceClientHistoriqueTable.personId eq idPersonne.id
        }
            .forEach {
                val syntheseModificationSer = SyntheseModificationSer(
                    traceAudit = TraceAuditSer(
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
