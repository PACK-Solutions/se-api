plugins {
    `jvm-test-suite`
}

testing {
    suites {
        register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())
            }
        }

    }
}

val integrationTestImplementation by configurations.getting

dependencies {
    integrationTestImplementation(project(":assembly"))
    integrationTestImplementation(project(":rest"))
    integrationTestImplementation(project(":database"))
    integrationTestImplementation(testFixtures(project(":core")))
    integrationTestImplementation(libs.testcontainers.postgresql)
    integrationTestImplementation(libs.bundles.kotest)
    integrationTestImplementation(libs.bundles.ktor)
    integrationTestImplementation(libs.bundles.exposed)
    integrationTestImplementation(libs.ktor.server.test.host)
    integrationTestImplementation(libs.ktor.client.content.negotiation)
    integrationTestImplementation(libs.json.path.kt)
    integrationTestImplementation(libs.hikaricp)
    integrationTestImplementation(libs.flyway.core)
    integrationTestImplementation(libs.flyway.postgres)

    detektPlugins(libs.detekt.formatting)
}
