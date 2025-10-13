dependencies {
    implementation(project(":core"))
    //TODO: Remove this dependency and move ExposedPersonRepository
    implementation(project(":rest"))
    implementation(testFixtures(project(":core")))

    // Exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)

    detektPlugins(libs.detekt.formatting)
}
