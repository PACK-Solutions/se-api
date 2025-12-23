dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))

    implementation(libs.bundles.ktor)

    implementation(libs.hikaricp)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)
    implementation(libs.bundles.exposed)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlin.logging)
    implementation(libs.logback)

    implementation(libs.kommand)

    detektPlugins(libs.detekt.formatting)

}

plugins {
    alias(libs.plugins.kotlin.serialization)
}

