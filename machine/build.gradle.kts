plugins {
    kotlin("plugin.jpa")
}

dependencies {
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-websocket")

    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("org.springframework.boot", "spring-boot-starter-validation")

    implementation("org.springframework.boot", "spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine", "caffeine")
    implementation("com.github.ben-manes.caffeine", "jcache")

    val openapi: String by project
    implementation("org.springdoc:springdoc-openapi-ui:$openapi")
    implementation("org.springdoc:springdoc-openapi-kotlin:$openapi")
    implementation("org.springdoc:springdoc-openapi-security:$openapi")

    runtimeOnly("com.h2database", "h2")
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.12.3")

    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    developmentOnly("org.springframework.boot", "spring-boot-devtools")

    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    implementation(kotlin("stdlib"))
}


repositories {
    mavenCentral()
}
