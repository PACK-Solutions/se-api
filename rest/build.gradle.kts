import org.gradle.jvm.tasks.Jar

dependencies {
    implementation(project(":assembly"))
    implementation(project(":core"))
    implementation(project(":database"))

    // Ktor
    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logback)
    detektPlugins(libs.detekt.formatting)
}

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

application {
    // NOTE: Move this main class to the assembly module
    mainClass.set("com.ps.personne.rest.AppKt")
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

/*tasks.withType<Jar>().configureEach {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version.toString(),
                "Implementation-Vendor" to "PACK Solutions"
            )
        )
    }
}*/

