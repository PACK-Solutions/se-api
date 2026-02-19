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

    implementation(libs.ps.framework.cqrs)
    implementation(libs.ps.framework.components)
    implementation(libs.ps.framework.ktor)

    detektPlugins(libs.detekt.formatting)

}

plugins {
    alias(libs.plugins.kotlin.serialization)
}


ktor {

    openApi {
        enabled = true
        codeInferenceEnabled = false
        onlyCommented = false
        debug = true

    }
}
