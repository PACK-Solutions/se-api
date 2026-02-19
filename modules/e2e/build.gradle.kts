dependencies {
    detektPlugins(libs.detekt.formatting)

    // DB drivers
    runtimeOnly(libs.postgres)
    runtimeOnly(libs.flyway.postgres)

    testImplementation(project(":assembly"))
    testImplementation(project(":infrastructure"))
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.bundles.kotest5)
    testImplementation(libs.bundles.ktor)
    testImplementation(libs.bundles.exposed)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.json.path.kt)
    testImplementation(libs.hikaricp)
    testImplementation(libs.flyway.core)
    testImplementation(libs.flyway.postgres)
    testImplementation(libs.selfie)
    testImplementation(libs.koin.ktor)
    testImplementation(testFixtures(project(":domain")))
    testImplementation((project(":application")))
    testImplementation(libs.ps.framework.components)
}

configurations.all {
    resolutionStrategy {
        force(libs.kotest5.assertions)
        force(libs.kotest5.engine)
        force(libs.kotest5.runner)
        force(libs.kotest5.extensions.clock)
    }
}

tasks.named<Test>("test") {
    enabled = false
}
tasks.register<Test>("e2eTest") {
    description = "Runs end-to-end tests."
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    shouldRunAfter("test")
}

