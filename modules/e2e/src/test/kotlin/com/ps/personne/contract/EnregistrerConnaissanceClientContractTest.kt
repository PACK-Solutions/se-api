package com.ps.personne.contract

import TestApp
import com.ps.personne.database.tables.ConnaissanceClientTable
import com.ps.personne.database.tables.HistoriqueTable
import io.kotest.core.spec.style.ShouldSpec
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import selfie.expectResponseSnapshot
import tenantId

class EnregistrerConnaissanceClientContractTest : ShouldSpec(
    {
        val client = TestApp.defaultClient

        beforeEach {
            transaction {
                ConnaissanceClientTable.deleteAll()
                HistoriqueTable.deleteAll()
            }
        }

        should("enregistrer une connaissance client") {
            expectResponseSnapshot(
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
                                "motifs": [
                                    "AGE_AVANCE",
                                    "OPERATION_COMPLEXE"
                                ],
                                "vigilanceRenforcee": true
                            }
                        }""",
                    )
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
                        "motifs": [
                            "AGE_AVANCE",
                            "OPERATION_COMPLEXE"
                        ],
                        "vigilanceRenforcee": true
                    }
                }
                ╔═ [ContentType] ═╗
                application/json; charset=UTF-8
                ╔═ [StatusCode] ═╗
                200""".trimIndent(),
            )
        }

        should("renvoyer une erreur si la connaissance client est invalide") {
            expectResponseSnapshot(
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
                                "vigilanceRenforcee": false
                            }
                        }
                        """,
                    )
                },
            ).toBe(
                """
                    {
                        "code": "CC_VRO",
                        "detail": "La vigilance renforcée est obligatoire pour un PPE ou un proche PPE",
                        "status": 400,
                        "title": "Bad Request",
                        "type": "about:blank"
                    }
                    ╔═ [ContentType] ═╗
                    application/problem+json
                    ╔═ [StatusCode] ═╗
                    400
                """.trimIndent(),
            )
        }
    },
)
