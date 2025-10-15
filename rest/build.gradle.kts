dependencies {
    implementation(project(":core"))
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logback)
    detektPlugins(libs.detekt.formatting)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.cors)

    // HikariCP
    implementation(libs.hikaricp)

    // Flyway
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)

    // Exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)

    // DB driver
    runtimeOnly(libs.postgres)
}

plugins{
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.openapi.gen)
    alias(libs.plugins.ktor)
}

//TODO: To configure for client SDK in first time
openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$rootDir/rest/src/main/resources/openapi/documentation.yaml")
    //TODO: Check deprecated
    outputDir.set("$buildDir/generated")
    apiPackage.set("com.ps.personne.rest.api")
    modelPackage.set("com.ps.personne.rest.model")
    //configOptions.put("dateLibrary", "java8")
}

openApiValidate {
    inputSpec.set("$rootDir/rest/src/main/resources/openapi/documentation.yaml")
    recommend.set(true)
}

application {
    //TODO: Move this main class to the assembly module
    mainClass.set("com.ps.personne.rest.AppKt")
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

