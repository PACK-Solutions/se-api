dependencies {
    implementation(project(":core"))
    implementation(project(":database"))

    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logback)

    detektPlugins(libs.detekt.formatting)
}

plugins {
    alias(libs.plugins.kotlin.serialization)
}
