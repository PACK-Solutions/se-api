plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":core"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.logging)
    implementation(libs.hikaricp)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)
    implementation(libs.bundles.exposed)

    // DB driver
    runtimeOnly(libs.postgres)

    testImplementation(libs.kotest.testcontainers)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.bundles.exposed)
    testImplementation(testFixtures(project(":core")))

    detektPlugins(libs.detekt.formatting)
}
