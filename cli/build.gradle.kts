plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
    jacoco
}

dependencies {
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.malinskiy.vdm.cli.MainKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
    kotlinOptions.apiVersion = libs.versions.kotlinApi.get()
}

tasks.test {
    useJUnitPlatform()
}
