plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    id("org.springframework.boot") version "2.6.4" apply false
    id("io.spring.dependency-management") version "1.1.0"
}

allprojects { repositories { mavenCentral(); mavenLocal(); google(); } }

subprojects {
    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            val java: String by project
            kotlinOptions { jvmTarget = java }
            test { useJUnitPlatform() }
        }
    }
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    dependencies {
        val springCloud: String by project
        implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:$springCloud"))
        annotationProcessor("org.springframework.boot", "spring-boot-starter-actuator")
        annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")
    }
}
defaultTasks("machine:bootRun")
