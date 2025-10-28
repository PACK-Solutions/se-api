plugins {
    `java-test-fixtures`
}

dependencies {
    implementation(libs.kotlin.result)
    testImplementation(libs.kotest.engine)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.runner)
    detektPlugins(libs.detekt.formatting)
}
