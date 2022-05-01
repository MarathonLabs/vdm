plugins {
    kotlin("jvm") version libs.versions.kotlin
    jacoco
}

dependencies {
    implementation(libs.junit5)
    implementation(libs.turtle)
    implementation(libs.adam)
    implementation(libs.coroutines)
    implementation(libs.androidSdkLib)
    implementation(libs.androidCommon)
    testImplementation(kotlin("test"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
    kotlinOptions.apiVersion = libs.versions.kotlinApi.get()
}

tasks.test {
    useJUnitPlatform()
}
