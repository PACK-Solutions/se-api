package com.ps.personne

import TestApp
import com.ps.personne.database.tables.ConnaissanceClientTable
import com.ps.personne.database.tables.HistoriqueTable
import com.ps.personne.fixtures.aConnaissanceClient
import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.LienParente
import com.ps.personne.rest.dto.request.ConnaissanceClientDto
import com.ps.personne.rest.dto.request.toDto
import io.kotest.core.spec.style.ShouldSpec
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import shouldHaveStatus
import tenantId

class HistoriqueConnaissanceClientTest : ShouldSpec(
    {
        val client = TestApp.defaultClient

        suspend fun getHistoriqueConnaissanceClient(id: Long, tenant: String = TestApp.defaultTenantId) =
            client.get("/personnes/$id/historique/connaissance-client") {
                tenantId(tenant)
            }

        suspend fun postConnaissanceClient(id: Long, connaissanceClientDto: ConnaissanceClientDto, tenant: String = TestApp.defaultTenantId) =
            client.post("/personnes/$id/connaissance-client") {
                tenantId(tenant)
                contentType(ContentType.Application.Json)
                setBody(connaissanceClientDto)
            }

        beforeEach {
            transaction {
                ConnaissanceClientTable.deleteAll()
                HistoriqueTable.deleteAll()
            }
        }

        should("enregistrer l'historique des modifications") {
            val id = 12345L
            val modif1 = aConnaissanceClient().withStatutPPE(
                FonctionPPE.DIRIGEANT_PARTI,
            ).withVigilanceRenforcee().build().toDto()
            val modif2 = aConnaissanceClient().withStatutProchePPE(
                LienParente.CONJOINT,
                FonctionPPE.DIRIGEANT_PARTI,
            ).withVigilanceRenforcee().build().toDto()

            postConnaissanceClient(id, modif1) shouldHaveStatus HttpStatusCode.OK
            postConnaissanceClient(id, modif2) shouldHaveStatus HttpStatusCode.OK

            val response = getHistoriqueConnaissanceClient(id)

            response shouldHaveStatus HttpStatusCode.OK
        }
    },
)
