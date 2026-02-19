dependencies {
    implementation(project(":domain"))
    implementation(libs.kotlin.logging)

    implementation(libs.ps.framework.cqrs)
    implementation(libs.ps.framework.components)

    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.bundles.kotest)
    testImplementation(testFixtures(project(":domain")))

}
