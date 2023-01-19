import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
}

group = "cn.rtast"
version = "1.2.1"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    val classValue = "cn.rtast.forwarder.MainKt"
    mainClass.set(classValue)
    mainClassName = classValue
}
