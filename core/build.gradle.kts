plugins {
    `java-test-fixtures`
}

dependencies {
    api(libs.kotlin.result)
    implementation(libs.logback)
    implementation(libs.kotlin.logging)
    testImplementation(libs.bundles.kotest)
    testFixturesImplementation(libs.bundles.kotest)
    detektPlugins(libs.detekt.formatting)
}
