plugins {
    kotlin("jvm") version libs.versions.kotlin
    jacoco
}

dependencies {
    implementation(libs.junit5)
    testImplementation(kotlin("test"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
    kotlinOptions.apiVersion = libs.versions.kotlinApi.get()
}

tasks.test {
    useJUnitPlatform()
}
