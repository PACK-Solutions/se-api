package com.ps.personne

import TestApp
import com.ps.personne.database.tables.ConnaissanceClientTable
import com.ps.personne.fixtures.aConnaissanceClient
import com.ps.personne.model.FonctionPPE
import com.ps.personne.rest.dto.request.ConnaissanceClientDto
import com.ps.personne.rest.dto.request.toDto
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

class RecupererConnaissanceClientTest : ShouldSpec(
    {
        val client = TestApp.defaultClient

        suspend fun getConnaissanceClient(id: Long, tenant: String = TestApp.defaultTenantId): ConnaissanceClientDto =
            client.get("/personnes/$id/connaissance-client") {
                header("tenantId", tenant)
            }.body()

        suspend fun postConnaissanceClient(id: Long, connaissanceClientDto: ConnaissanceClientDto, tenant: String = TestApp.defaultTenantId) =
            client.post("/personnes/$id/connaissance-client") {
                header("tenantId", tenant)
                contentType(ContentType.Application.Json)
                setBody(connaissanceClientDto)
            }

        beforeEach {
            transaction {
                ConnaissanceClientTable.deleteAll()
            }
        }

        should("renvoyer un status 200 lors de la recuperation d'une connaissance client") {
            client.get("/personnes/12345/connaissance-client").status shouldBe HttpStatusCode.OK
        }

        should("recuperer une connaissance client pour un client donné") {
            val id = 12345L
            val connaissanceClientDto = aConnaissanceClient().withStatutPPE(
                FonctionPPE.DIRIGEANT_PARTI,
            ).withVigilanceRenforcee().build().toDto()
            postConnaissanceClient(id, connaissanceClientDto)

            val response = getConnaissanceClient(id)

            response shouldBe connaissanceClientDto
        }

        should("recuperer la connaissance client sur le bon tenant") {
            val id = 12345L
            val connaissanceClientTenant1 = aConnaissanceClient().withStatutPPE(
                FonctionPPE.DIRIGEANT_PARTI,
            ).withVigilanceRenforcee().build().toDto()
            val connaissanceClientTenant2 = aConnaissanceClient().withStatutPPE(
                FonctionPPE.MEMBRE_PARLEMENT,
            ).withVigilanceRenforcee().build().toDto()

            postConnaissanceClient(id, connaissanceClientTenant1, "tenant1")
            postConnaissanceClient(id, connaissanceClientTenant2, "tenant2")

            getConnaissanceClient(id, "tenant1") shouldBe connaissanceClientTenant1
            getConnaissanceClient(id, "tenant2") shouldBe connaissanceClientTenant2
        }

        should("renvoyer une erreur 404 si l'id n'est pas fourni") {
            client.get("/personnes//connaissance-client").status shouldBe HttpStatusCode.NotFound
        }

        should("renvoyer une erreur 400 si l'id n'est pas au format attendu") {
            client.get("/personnes/pwet/connaissance-client").status shouldBe HttpStatusCode.BadRequest
        }
    },
)
