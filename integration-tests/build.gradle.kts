plugins {
    `jvm-test-suite`
}

testing {
    suites {

        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }

    }
}

val integrationTestApi by configurations.getting

dependencies {
    integrationTestApi(project(":assembly"))
    integrationTestApi(project(":rest"))
    integrationTestApi(project(":database"))

    integrationTestApi(testFixtures(project(":core")))

    integrationTestApi(libs.testcontainers.postgresql)
    integrationTestApi(libs.bundles.kotest)
    integrationTestApi(libs.bundles.ktor)
    integrationTestApi(libs.bundles.exposed)
    integrationTestApi(libs.ktor.server.test.host)
    integrationTestApi(libs.ktor.client.content.negotiation)
    integrationTestApi(libs.json.path.kt)
    integrationTestApi(libs.kotlinx.serialization.json)
    integrationTestApi(libs.hikaricp)
    integrationTestApi(libs.flyway.core)
    integrationTestApi(libs.flyway.postgres)



    detektPlugins(libs.detekt.formatting)
}
