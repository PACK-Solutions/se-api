import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import nl.littlerobots.vcu.plugin.resolver.VersionSelectors

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotest) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.versions)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    // Apply common plugins to all subprojects
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.kotest")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "version-catalog")

    // Configure Java toolchain for all JVM projects
    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    // Detekt configuration
    extensions.configure<DetektExtension> {
        config.setFrom("${rootProject.projectDir}/detekt.yml")
        buildUponDefaultConfig = true
        autoCorrect = true
    }


    // Ensure tests run on JUnit Platform (required by Kotest)
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

// Task to setup code conventions in the root project
val setupCodeConventions = tasks.register("setupCodeConventions", SetupCodeConventionsTask::class) {
    group = "setup"
    description = "Download .editorconfig and detekt.yml into the project root from PACK-Solutions/Kotlin-conventions repo"
    rootDir.set(rootProject.layout.projectDirectory)
    overwrite.set(providers.gradleProperty("overwrite").orNull)
}

versionCatalogUpdate {
    versionSelector(VersionSelectors.STABLE)
}
