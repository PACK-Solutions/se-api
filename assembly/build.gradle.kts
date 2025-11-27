import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "com.ps"
version = rootProject.version as String

dependencies {
    implementation(project(":core"))
    implementation(project(":database"))
    implementation(project(":rest"))
    implementation(libs.bundles.ktor)

    detektPlugins(libs.detekt.formatting)

    // DB drivers
    runtimeOnly(libs.postgres)
    runtimeOnly(libs.flyway.postgres)
}

plugins {
    alias(libs.plugins.ktor)
}

application {
    mainClass.set("com.ps.personne.AppKt")
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

// Mandatory to include Flyway migrations scripts
tasks.withType<ShadowJar>().configureEach {
    mergeServiceFiles()
    filesMatching("META-INF/services/**") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version.toString(),
                "Implementation-Vendor" to "PACK Solutions",
            ),
        )
    }
}
