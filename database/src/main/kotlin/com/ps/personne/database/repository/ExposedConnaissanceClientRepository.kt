package com.ps.personne.database.repository

import com.github.michaelbull.result.Ok
import com.ps.personne.database.mapper.toDomain
import com.ps.personne.database.mapper.toSer
import com.ps.personne.database.model.SyntheseModificationSer
import com.ps.personne.database.model.TraceAuditSer
import com.ps.personne.database.tables.ConnaissanceClientTable
import com.ps.personne.database.tables.ConnaissanceClientTable.personId
import com.ps.personne.database.tables.HistoriqueModificationConnaissanceClientTable
import com.ps.personne.database.tables.HistoriqueModificationConnaissanceClientTable.auditDate
import com.ps.personne.database.tables.HistoriqueModificationConnaissanceClientTable.auditType
import com.ps.personne.database.tables.HistoriqueModificationConnaissanceClientTable.auditUser
import com.ps.personne.database.tables.HistoriqueModificationConnaissanceClientTable.modifications
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.HistoriqueModifications
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.SyntheseModifications
import com.ps.personne.ports.driven.ConnaissanceClientRepository
import com.ps.personne.ports.driven.ModificationsConnaissanceClientRepository
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
            HistoriqueModificationConnaissanceClientTable.insert {
                it[id] = UUID.randomUUID()
                it[personId] = connaissanceClient.idPersonne.id
                it[auditUser] = modificationSer.traceAudit.user
                it[auditType] = modificationSer.traceAudit.typeOperation
                it[auditDate] = modificationSer.traceAudit.date
                it[modifications] = modificationSer.modifications
            }
        }

        Ok(connaissanceClient.idPersonne)
    }

    override fun recupererHistorique(idPersonne: IdPersonne) = transaction {
        var entreesHistorique = emptyList<SyntheseModifications>()
        HistoriqueModificationConnaissanceClientTable.selectAll().where {
            HistoriqueModificationConnaissanceClientTable.personId eq idPersonne.id
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
        Ok(HistoriqueModifications(idPersonne, entreesHistorique))
    }
}
