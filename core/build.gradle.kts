plugins {
    `java-test-fixtures`
}

dependencies {
    testImplementation(libs.kotest.engine)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.runner)
    detektPlugins(libs.detekt.formatting)
}
