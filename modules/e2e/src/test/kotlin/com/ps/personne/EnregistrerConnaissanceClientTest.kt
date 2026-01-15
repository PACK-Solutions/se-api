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
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import shouldHaveStatus
import shouldReturn
import tenantId

class EnregistrerConnaissanceClientTest : ShouldSpec(
    {


        val client = TestApp.defaultClient
        suspend fun getConnaissanceClient(id: Long, tenant: String = TestApp.defaultTenantId): HttpResponse =
            client.get("/personnes/$id/connaissance-client") {
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

        should("enregistrer une connaissance client pour un client donné") {
            val id = 12345L
            val connaissanceClientDto = aConnaissanceClient()
                .withStatutPPE(FonctionPPE.DIRIGEANT_PARTI)
                .withVigilanceRenforcee()
                .build().toDto()

            postConnaissanceClient(id, connaissanceClientDto) shouldHaveStatus HttpStatusCode.OK

            getConnaissanceClient(id) shouldReturn connaissanceClientDto
        }

        should("enregistrer la connaissance client sur le bon tenant") {
            val id = 12345L
            val connaissanceClientTenant1 = aConnaissanceClient()
                .withStatutPPE(FonctionPPE.DIRIGEANT_PARTI)
                .withVigilanceRenforcee()
                .build().toDto()
            val connaissanceClientTenant2 = aConnaissanceClient()
                .withStatutPPE(FonctionPPE.MEMBRE_PARLEMENT)
                .withVigilanceRenforcee()
                .build().toDto()

            postConnaissanceClient(id, connaissanceClientTenant1, "tenant1") shouldHaveStatus HttpStatusCode.OK
            postConnaissanceClient(id, connaissanceClientTenant2, "tenant2") shouldHaveStatus HttpStatusCode.OK

            getConnaissanceClient(id, "tenant1") shouldHaveStatus HttpStatusCode.OK shouldReturn connaissanceClientTenant1
            getConnaissanceClient(id, "tenant2") shouldHaveStatus HttpStatusCode.OK shouldReturn connaissanceClientTenant2
        }

        should("ne pas enregistrer si la connaissance client est invalide et renvoyer une erreur") {
            val id = 12345L
            val connaissanceClientTenant1 = aConnaissanceClient()
                .withStatutPPE(FonctionPPE.DIRIGEANT_PARTI)
                .withoutVigilanceRenforcee()
                .build().toDto()

            postConnaissanceClient(id, connaissanceClientTenant1) shouldHaveStatus HttpStatusCode.BadRequest

            getConnaissanceClient(id) shouldReturn aConnaissanceClient().defaultValue().toDto()
        }

        should("renvoyer une erreur 404 si l'id n'est pas fourni") {
            client.post("/personnes//connaissance-client") {
                contentType(ContentType.Application.Json)
                setBody(aConnaissanceClient().defaultValue().toDto())
            }.status shouldBe HttpStatusCode.NotFound
        }

        should("renvoyer une erreur 400 si l'id n'est pas au format attendu") {
            client.post("/personnes/pwet/connaissance-client") {
                contentType(ContentType.Application.Json)
                setBody(aConnaissanceClient().defaultValue().toDto())
            }.status shouldBe HttpStatusCode.BadRequest
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

            postConnaissanceClient(id, modif1)
            postConnaissanceClient(id, modif2)

            transaction {
                HistoriqueTable.selectAll().count() shouldBe 2
            }
        }
    },
)
