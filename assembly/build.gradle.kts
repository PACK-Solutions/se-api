group = "com.ps"
version = rootProject.version as String

dependencies {
    implementation(project(":core"))
    implementation(project(":database"))
    // Ktor
    implementation(libs.bundles.ktor)
    detektPlugins(libs.detekt.formatting)
}
