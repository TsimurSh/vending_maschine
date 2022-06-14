description = "GATEWAY"

dependencies {
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud", "spring-cloud-starter-gateway")

    annotationProcessor("org.springframework.boot", "spring-boot-starter-actuator")

    testImplementation("org.springframework.boot", "spring-boot-starter-test")
}
