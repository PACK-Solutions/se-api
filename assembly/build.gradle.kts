import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "com.ps"
version = rootProject.version as String

plugins {
    alias(libs.plugins.ktor)
}

sourceSets {
    val e2e by creating {
        kotlin.srcDir("src/e2e/kotlin")
        resources.srcDir("src/e2e/resources")
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":database"))
    implementation(project(":rest"))
    implementation(libs.bundles.ktor)
    implementation(libs.kommand)
    implementation(libs.kotlin.logging)

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
    e2eImplementation(testFixtures(project(":core")))
}

tasks.register<Test>("e2eTest") {
    description = "Runs end-to-end tests."
    group = "verification"
    testClassesDirs = sourceSets["e2e"].output.classesDirs
    classpath = sourceSets["e2e"].runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter("test")
}


application {
    mainClass.set("com.ps.personne.AppKt")
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

// Mandatory to include Flyway migrations scripts
tasks.withType<ShadowJar>().configureEach {
    mergeServiceFiles()
    filesMatching("META-INF/services/**") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version.toString(),
                "Implementation-Vendor" to "PACK Solutions",
            ),
        )
    }
}

tasks.named<Copy>("processE2eResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
