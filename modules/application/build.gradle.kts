dependencies {
    implementation(project(":domain"))
    implementation(libs.kommand)

    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.bundles.kotest)
    testImplementation(testFixtures(project(":domain")))

}
