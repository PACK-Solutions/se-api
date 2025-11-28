package com.ps.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class CodeConventionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // Register the SetupCodeConventionsTask on the project where the plugin is applied
        target.tasks.register<SetupCodeConventionsTask>("setupCodeConventions") {
            group = "setup"
            description = "Download .editorconfig and detekt.yml into the project root from PACK-Solutions/Kotlin-conventions repo"
            rootDir.set(target.rootProject.layout.projectDirectory)
            overwrite.set(target.providers.gradleProperty("overwrite").orNull)
        }
    }
}
