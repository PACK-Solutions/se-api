dependencies {
    implementation(project(":domain"))
    implementation(libs.kommand)
    implementation(libs.kotlin.logging)

    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.bundles.kotest)
    testImplementation(testFixtures(project(":domain")))

}
