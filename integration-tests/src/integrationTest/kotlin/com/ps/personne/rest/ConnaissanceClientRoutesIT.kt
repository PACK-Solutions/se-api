package com.ps.personne.rest

import com.nfeld.jsonpathkt.kotlinx.resolvePathAsStringOrNull
import com.nfeld.jsonpathkt.kotlinx.resolvePathOrNull
import com.ps.personne.kyc.dto.request.*
import com.ps.personne.personne
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.*
import org.testcontainers.postgresql.PostgreSQLContainer

class ConnaissanceClientRoutesIT : BehaviorSpec(
    {

        val pg = PostgreSQLContainer("postgres:18")

        val applicationConfig by lazy {
            MapApplicationConfig(
                "database.url" to pg.jdbcUrl,
                "database.user" to pg.username,
                "database.password" to pg.password,
                "database.driverClassName" to "org.postgresql.Driver",
                "database.maximumPoolSize" to "5",
                "database.minimumIdle" to "1",
                "database.idleTimeout" to "600000",
                "database.connectionTimeout" to "30000",
                "database.maxLifetime" to "1800000",
            )
        }

        beforeSpec {
            pg.start()
        }

        afterSpec {
            pg.stop()
        }

        given("Étant donné une API Connaissance Client reliée à une base Postgres") {

            val defaultPayload = ConnaissanceClientDto(
                statutPPE = null,
                statutProchePPE = null,
                vigilance = VigilanceDto(
                    vigilanceRenforcee = false,
                ),
            )

            val payload = ConnaissanceClientDto(
                statutPPE = PpeDto(
                    mandat = MandatDto(
                        fonction = "CHEF_ETAT",
                    ),
                ),
                statutProchePPE = ProchePpeDto(
                    lienParente = "CONJOINT",
                    mandat = MandatDto(
                        fonction = "MEMBRE_GOUVERNEMENT",
                        dateFin = "2030-12-31",
                    ),
                ),
                vigilance = VigilanceDto(
                    vigilanceRenforcee = true,
                    motifs = listOf("MONTANT_ELEVE", "OPERATION_COMPLEXE"),
                ),
            )

            testApplication {
                environment { config = applicationConfig }
                application { personne() }

                val client = createClient { install(ContentNegotiation) { json() } }

                `when`("je crée la connaissance client pour une personne avec les valeurs par défaut") {
                    then("alors la création réussit avec 304 Not Modified") {
                        val id = 12345
                        val postResponse = client.post("/personnes/$id/connaissance-client") {
                            header("login", "john.doe")
                            header("tenantId", "pack")
                            contentType(ContentType.Application.Json)
                            setBody(defaultPayload)
                        }
                        postResponse.status shouldBe HttpStatusCode.NotModified
                    }
                }

                `when`("je crée la connaissance client pour une personne") {
                    then("alors la création réussit (201 Created)") {
                        val id = 12345
                        val postResponse = client.post("/personnes/$id/connaissance-client") {
                            header("login", "john.doe")
                            header("tenantId", "pack")
                            contentType(ContentType.Application.Json)
                            setBody(payload)
                        }
                        postResponse.status shouldBe HttpStatusCode.Created
                    }
                }

                `when`("je consulte la connaissance client précédemment créée") {
                    then("alors je reçois 200 OK et la vigilance renforcée est vraie") {
                        val id = 12345
                        val getResponse = client.get("/personnes/$id/connaissance-client") {
                            header("tenantId", "pack")
                            header("login", "john.doe")
                        }
                        getResponse.status shouldBe HttpStatusCode.OK
                        val body = getResponse.bodyAsText()
                        val json = Json.parseToJsonElement(body)
                        val flag = json.resolvePathAsBooleanOrNull("$.vigilance.vigilanceRenforcee")
                        flag shouldBe true
                    }
                }

                `when`("je consulte l'historique des modifications après une création") {
                    then(
                        "alors l'historique renvoie 200 OK et contient une entree avec la trace d'audit",
                    ) {
                        val id = 54321L
                        val postResponse = client.post("/personnes/$id/connaissance-client") {
                            header("login", "jane.doe")
                            header("tenantId", "pack")
                            contentType(ContentType.Application.Json)
                            setBody(payload)
                        }
                        postResponse.status shouldBe HttpStatusCode.Created

                        val histoResponse = client.get("/personnes/$id/historique-connaissance-client") {
                            header("tenantId", "pack")
                            header("login", "jane.doe")
                        }
                        histoResponse.status shouldBe HttpStatusCode.OK
                        val histoBody = histoResponse.bodyAsText()
                        val histoJson = Json.parseToJsonElement(histoBody)
                        val idPersonne = histoJson.resolvePathAsLongOrNull("$.idPersonne")
                        idPersonne shouldBe id
                        val entrees = histoJson.resolvePathOrNull("$.entreesHistorique")?.jsonArray
                        entrees.shouldNotBeEmpty()

                        val user = histoJson.resolvePathAsStringOrNull("$.entreesHistorique[0].traceAudit.user")
                        user shouldBe "jane.doe"
                        val typeOperation = histoJson.resolvePathAsStringOrNull(
                            "$.entreesHistorique[0].traceAudit.typeOperation",
                        )
                        typeOperation shouldBe "MODIFICATION"
                        val modifications = histoJson.resolvePathOrNull(
                            "$.entreesHistorique[0].modifications",
                        )?.jsonArray
                        modifications.shouldNotBeEmpty()
                    }
                }

                `when`("je consulte l'historique d'une personne sans historique connaissance client") {
                    then("alors je reçois 200 OK avec une liste vide") {
                        val idInconnu = 999_888
                        val histoResponse = client.get("/personnes/$idInconnu/historique-connaissance-client") {
                            header("tenantId", "pack")
                            header("login", "anonymous")
                        }
                        histoResponse.status shouldBe HttpStatusCode.OK
                        val histoBody = histoResponse.bodyAsText()
                        val histoJson = Json.parseToJsonElement(histoBody)
                        val entrees = histoJson.resolvePathOrNull("$.entreesHistorique")?.jsonArray
                        entrees.shouldBeEmpty()
                    }
                }

                `when`("je consulte la connaissance client d'une personne sans connaissance client") {
                    then("alors je reçois une connaissance client vierge") {
                        val idPersonne = 999999L
                        val getResponse = client.get("/personnes/$idPersonne/connaissance-client") {
                            header("tenantId", "pack")
                            header("login", "anonymous")
                        }
                        getResponse.status shouldBe HttpStatusCode.OK
                        val connaissanceClientBody = getResponse.bodyAsText()
                        val connaissanceClientJson = Json.parseToJsonElement(connaissanceClientBody)
                        val isPpe = connaissanceClientJson.resolvePathAsBooleanOrNull("$.statutPPE")
                        val isProchePpe = connaissanceClientJson.resolvePathAsBooleanOrNull("$.statutProchePPE")
                        val vigilance = connaissanceClientJson.resolvePathAsBooleanOrNull("$.vigilance.vigilanceRenforcee")
                        isPpe shouldBe null
                        isProchePpe shouldBe null
                        vigilance shouldBe false
                    }
                }

                `when`("je tente de créer sans header login") {
                    then("alors la requête est rejetée (400 Bad Request)") {
                        val response = client.post("/personnes/42/connaissance-client") {
                            contentType(ContentType.Application.Json)
                            setBody(payload)
                        }
                        response.status shouldBe HttpStatusCode.BadRequest
                    }
                }
            }
        }
    },
)

private fun JsonElement.resolvePathAsBooleanOrNull(path: String) =
    resolvePathOrNull(path)?.jsonPrimitive?.booleanOrNull

private fun JsonElement.resolvePathAsLongOrNull(path: String) =
    resolvePathOrNull(path)?.jsonPrimitive?.longOrNull
