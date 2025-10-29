plugins {
    `java-test-fixtures`
}

dependencies {
    api(libs.kotlin.result)
    testImplementation(libs.bundles.kotest)
    detektPlugins(libs.detekt.formatting)
}
