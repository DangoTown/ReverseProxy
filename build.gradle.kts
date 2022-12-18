import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
}

group = "cn.rtast.forwarder"
version = "1.2"

repositories {
    maven { setUrl("https://maven.aliyun.com/repository/public") }
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    val classValue = "forwarder.StreamForwarderKt"
    mainClass.set(classValue)
    mainClassName = classValue
}
