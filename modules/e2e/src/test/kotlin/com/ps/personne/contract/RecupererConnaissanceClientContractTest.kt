package com.ps.personne.contract

import TestApp
import com.ps.personne.database.tables.ConnaissanceClientTable
import com.ps.personne.database.tables.HistoriqueTable
import io.kotest.core.spec.style.ShouldSpec
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import selfie.expectResponseSnapshot
import shouldHaveStatus
import tenantId

class RecupererConnaissanceClientContractTest : ShouldSpec(
    {
        val client = TestApp.defaultClient

        beforeEach {
            transaction {
                ConnaissanceClientTable.deleteAll()
                HistoriqueTable.deleteAll()
            }
        }

        should("recuperer une connaissance client") {
            client.post("/personnes/12345/connaissance-client") {
                contentType(ContentType.Application.Json)
                tenantId("client1")
                setBody(
                    """
                        {
                            "statutPPE": {
                                "mandat": {
                                    "fonction": "DIRIGEANT_PARTI"
                                }
                            },
                            "vigilance": {
                                "vigilanceRenforcee": true
                            }
                        }""",
                )
            } shouldHaveStatus HttpStatusCode.OK

            expectResponseSnapshot(
                client.get("/personnes/12345/connaissance-client") {
                    contentType(ContentType.Application.Json)
                    tenantId("client1")
                },
            ).toBe(
                """
                {
                    "statutPPE": {
                        "mandat": {
                            "fonction": "DIRIGEANT_PARTI"
                        }
                    },
                    "vigilance": {
                        "motifs": [],
                        "vigilanceRenforcee": true
                    }
                }
                ╔═ [ContentType] ═╗
                application/json; charset=UTF-8
                ╔═ [StatusCode] ═╗
                200""".trimIndent(),
            )

        }

    },
)
