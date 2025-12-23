package com.ps.personne

import com.ps.personne.fixtures.aConnaissanceClient
import com.ps.personne.kyc.dto.request.ConnaissanceClientDto
import com.ps.personne.kyc.dto.request.toDto
import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.LienParente
import com.ps.personne.tables.ConnaissanceClientTable
import com.ps.personne.tables.HistoriqueTable
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.provided.KtorTestApp
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class EnregistrerConnaissanceClientTest : ShouldSpec(
    {
        suspend fun getConnaissanceClient(id: Long, tenant: String = KtorTestApp.defaultTenantId): ConnaissanceClientDto =
            KtorTestApp.defaultHttpClient.get("/personnes/$id/connaissance-client") {
                header("tenantId", tenant)
            }.body()

        suspend fun postConnaissanceClient(id: Long, connaissanceClientDto: ConnaissanceClientDto, tenant: String = KtorTestApp.defaultTenantId) =
            KtorTestApp.defaultHttpClient.post("/personnes/$id/connaissance-client") {
                header("tenantId", tenant)
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
            val connaissanceClientDto = aConnaissanceClient().withStatutPPE(FonctionPPE.DIRIGEANT_PARTI).withVigilanceRenforcee().build().toDto()

            val postResponse = postConnaissanceClient(id, connaissanceClientDto)

            postResponse.status shouldBe HttpStatusCode.OK
            getConnaissanceClient(id) shouldBe connaissanceClientDto
        }

        should("enregistrer la connaissance client sur le bon tenant") {
            val id = 12345L
            val connaissanceClientTenant1 = aConnaissanceClient().withStatutPPE(FonctionPPE.DIRIGEANT_PARTI).withVigilanceRenforcee().build().toDto()
            val connaissanceClientTenant2 = aConnaissanceClient().withStatutPPE(FonctionPPE.MEMBRE_PARLEMENT).withVigilanceRenforcee().build().toDto()

            postConnaissanceClient(id, connaissanceClientTenant1, "tenant1")
            postConnaissanceClient(id, connaissanceClientTenant2, "tenant2")

            getConnaissanceClient(id, "tenant1") shouldBe connaissanceClientTenant1
            getConnaissanceClient(id, "tenant2") shouldBe connaissanceClientTenant2
        }

        should("ne pas enregistrer si la connaissance client est invalide et renvoyer une erreur") {
            val id = 12345L
            val connaissanceClientTenant1 = aConnaissanceClient().withStatutPPE(FonctionPPE.DIRIGEANT_PARTI).withoutVigilanceRenforcee().build().toDto()

            val result = postConnaissanceClient(id, connaissanceClientTenant1)

            result.status shouldBe HttpStatusCode.BadRequest

            getConnaissanceClient(id) shouldBe aConnaissanceClient().defaultValue().toDto()
        }


        should("renvoyer une erreur 404 si l'id n'est pas fourni") {
            KtorTestApp.defaultHttpClient.post("/personnes//connaissance-client") {
                contentType(ContentType.Application.Json)
                setBody(aConnaissanceClient().defaultValue().toDto())
            }.status shouldBe HttpStatusCode.NotFound
        }

        should("renvoyer une erreur 400 si l'id n'est pas au format attendu") {
            KtorTestApp.defaultHttpClient.post("/personnes/pwet/connaissance-client") {
                contentType(ContentType.Application.Json)
                setBody(aConnaissanceClient().defaultValue().toDto())
            }.status shouldBe HttpStatusCode.BadRequest
        }

        should("enregistrer l'historique des modifications") {
            val id = 12345L
            val modif1 = aConnaissanceClient().withStatutPPE(FonctionPPE.DIRIGEANT_PARTI).withVigilanceRenforcee().build().toDto()
            val modif2 = aConnaissanceClient().withStatutProchePPE(LienParente.CONJOINT, FonctionPPE.DIRIGEANT_PARTI).withVigilanceRenforcee().build().toDto()

            postConnaissanceClient(id, modif1)
            postConnaissanceClient(id, modif2)


            transaction {
                HistoriqueTable.selectAll().count() shouldBe 2
            }

        }
    },
)
