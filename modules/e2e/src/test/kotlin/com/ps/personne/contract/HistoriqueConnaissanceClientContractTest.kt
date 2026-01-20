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

class HistoriqueConnaissanceClientContractTest : ShouldSpec(
    {
        val client = TestApp.defaultClient

        beforeEach {
            transaction {
                ConnaissanceClientTable.deleteAll()
                HistoriqueTable.deleteAll()
            }
        }

        should("recuperer un historique connaissance client") {
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

            client.post("/personnes/12345/connaissance-client") {
                contentType(ContentType.Application.Json)
                tenantId("client1")
                setBody(
                    """
                        {
                            "statutPPE": {
                                "mandat": {
                                    "fonction": "DIRIGEANT_PARTI"
                                    "dateFin": "2028-01-01"
                                }
                            },
                            "statutProchePPE": {
                                "mandat": {
                                    "fonction": "DIRIGEANT_PARTI"
                                    "dateFin": "2029-01-01"
                                },
                                "lienParente": "CONJOINT"
                            }
                            "vigilance": {
                                "vigilanceRenforcee": true
                                "motifs": ["AGE_AVANCE", "OPERATION_COMPLEXE"]
                            }
                        }""",
                )
            } shouldHaveStatus HttpStatusCode.OK

            expectResponseSnapshot(
                client.get("/personnes/12345/historique/connaissance-client") {
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
                },
            ).toBe(
                """
[
    {
        "changements": [
            {
                "newValue": "DIRIGEANT_PARTI",
                "proprieteObjet": "FonctionPPE",
                "type": "Creation"
            },
            {
                "newValue": "true",
                "oldValue": "false",
                "proprieteObjet": "Vigilance",
                "type": "Modification"
            }
        ],
        "idObjet": "12345",
        "occurredAt": "2026-01-20T10:30:19.313712Z",
        "performedBy": "unknown"
    },
    {
        "changements": [
            {
                "newValue": "2028-01-01",
                "proprieteObjet": "DateFinFonctionPPE",
                "type": "Creation"
            },
            {
                "newValue": "[AGE_AVANCE, OPERATION_COMPLEXE]",
                "proprieteObjet": "MotifVigilance",
                "type": "Creation"
            },
            {
                "newValue": "CONJOINT",
                "proprieteObjet": "LienParenteProchePPE",
                "type": "Creation"
            },
            {
                "newValue": "DIRIGEANT_PARTI",
                "proprieteObjet": "FonctionProchePPE",
                "type": "Creation"
            },
            {
                "newValue": "2029-01-01",
                "proprieteObjet": "DateFinFonctionProchePPE",
                "type": "Creation"
            }
        ],
        "idObjet": "12345",
        "occurredAt": "2026-01-20T10:30:19.319073Z",
        "performedBy": "unknown"
    }
]
╔═ [ContentType] ═╗
application/json; charset=UTF-8
╔═ [StatusCode] ═╗
200""",
            )


        }

    },
)
