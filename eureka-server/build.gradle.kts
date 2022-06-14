description = "EUREKA-SERVER"

dependencies {
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-server")
    implementation("com.github.ben-manes.caffeine","caffeine")
    implementation("com.github.ben-manes.caffeine", "jcache")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")

    testImplementation("org.springframework.boot", "spring-boot-starter-test")
}
