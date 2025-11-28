package com.ps.conventions

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException

abstract class SetupCodeConventionsTask : DefaultTask() {

    @get:Internal
    abstract val rootDir: DirectoryProperty

    @get:Optional
    @get:Input
    abstract val overwrite: Property<String>

    init {
        group = "setup"
        description = "Download .editorconfig and detekt.yml into the project root from PACK-Solutions/Kotlin-conventions repo"
    }

    @TaskAction
    fun run() {
        val rootDirFile: File = rootDir.get().asFile
        val targets = listOf(
            ".editorconfig" to "https://raw.githubusercontent.com/PACK-Solutions/Kotlin-conventions/main/.editorconfig",
            "detekt.yml" to "https://raw.githubusercontent.com/PACK-Solutions/Kotlin-conventions/main/detekt.yml",
        )

        val overwriteProp = overwrite.orNull?.lowercase()?.let {
            when (it) {
                "true", "yes", "y" -> true
                "false", "no", "n" -> false
                else -> null
            }
        }

        fun shouldOverwrite(file: File): Boolean {
            if (!file.exists()) return true
            if (overwriteProp != null) return overwriteProp
            logger.lifecycle(
                "{} already exists. Run with -Poverwrite=true to overwrite, -Poverwrite=false to skip. Skipping by default.",
                file.name
            )
            return false
        }

        targets.forEach { (name, urlStr) ->
            val dest = File(rootDirFile, name)
            try {
                if (shouldOverwrite(dest)) {
                    URI(urlStr).toURL().openStream().use { input ->
                        dest.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    logger.lifecycle("Downloaded {} to {}", name, dest)
                } else {
                    logger.lifecycle("Skipped {}", name)
                }
            } catch (e: IOException) {
                throw GradleException("Failed to download $name from $urlStr: ${e.message}", e)
            } catch (e: URISyntaxException) {
                throw GradleException("Invalid URL for $name: $urlStr", e)
            }
        }
    }
}
