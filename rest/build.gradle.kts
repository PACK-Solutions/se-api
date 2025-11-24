dependencies {
    implementation(project(":core"))
    implementation(project(":database"))

    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logback)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.json.path.kt)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.testcontainers.postgresql)
    // TODO even in test, we should not import assembly
    testImplementation(project(":assembly"))
    detektPlugins(libs.detekt.formatting)
}

plugins {
    alias(libs.plugins.kotlin.serialization)
}

