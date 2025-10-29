dependencies {
    implementation(project(":core"))
    implementation(testFixtures(project(":core")))

    // Exposed
    implementation(libs.bundles.exposed)

    detektPlugins(libs.detekt.formatting)
}
