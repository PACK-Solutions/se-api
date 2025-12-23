sourceSets {
    val e2e by creating {
        kotlin.srcDir("src/e2e/kotlin")
        resources.srcDir("src/e2e/resources")
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    implementation(libs.kommand)

    detektPlugins(libs.detekt.formatting)

    // DB drivers
    runtimeOnly(libs.postgres)
    runtimeOnly(libs.flyway.postgres)

    val e2eImplementation by configurations.getting
    e2eImplementation(libs.testcontainers.postgresql)
    e2eImplementation(libs.bundles.kotest)
    e2eImplementation(libs.bundles.ktor)
    e2eImplementation(libs.bundles.exposed)
    e2eImplementation(libs.ktor.server.test.host)
    e2eImplementation(libs.ktor.client.content.negotiation)
    e2eImplementation(libs.json.path.kt)
    e2eImplementation(libs.hikaricp)
    e2eImplementation(libs.flyway.core)
    e2eImplementation(libs.flyway.postgres)
    e2eImplementation(testFixtures(project(":domain")))
}

tasks.register<Test>("e2eTest") {
    description = "Runs end-to-end tests."
    group = "verification"
    testClassesDirs = sourceSets["e2e"].output.classesDirs
    classpath = sourceSets["e2e"].runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter("test")
}

tasks.named<Copy>("processE2eResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
