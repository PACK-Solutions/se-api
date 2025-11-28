plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        create("codeConventions") {
            id = "com.ps.code-conventions"
            implementationClass = "com.ps.conventions.CodeConventionsPlugin"
            displayName = "PS Code Conventions Plugin"
            description = "Registers tasks to setup common code convention files"
        }
    }
}
