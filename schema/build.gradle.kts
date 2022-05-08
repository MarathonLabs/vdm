import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegenPlugin
import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegen // only
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.jsonKotlinGradle)
    }
}

plugins {
    kotlin("jvm") version libs.versions.kotlin
    jacoco
}

sourceSets.main {
    java.srcDirs("build/generated-sources/kotlin")
}

apply<JSONSchemaCodegenPlugin>()

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
    kotlinOptions.apiVersion = libs.versions.kotlinApi.get()
}

tasks.test {
    useJUnitPlatform()
}

configure<JSONSchemaCodegen> {
    packageName.set("com.malinskiy.vdm")
    inputs {
        inputFile.set(file("${project.projectDir}/src/main/resources/schema"))
    }
    outputDir.set(file("${project.buildDir}/generated-sources/kotlin"))
}

dependencies {
    implementation(libs.jsonSchemaValidator)
    implementation(libs.jacksonYaml)
    implementation(libs.jacksonDatabind)
    implementation(libs.jacksonKotlin)
    testImplementation(libs.junit5)
}