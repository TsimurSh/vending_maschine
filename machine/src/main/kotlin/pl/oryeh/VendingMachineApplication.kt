package pl.oryeh

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableCaching
@EnableEurekaClient
@ConfigurationPropertiesScan
@OpenAPIDefinition(
    servers = [Server(url = "http://localhost:8080")],
    info = Info(
        contact = Contact(
            name = "Tsimur Shykhsefiyeu ", email = "tim.oryeh@gmail.com",
            url = "https://github.com/TsimurSh"
        ),
        description = "Vending application", title = "Vending machine", version = "1.0"
    )
)
@SpringBootApplication
class VendingMachineApplication

fun main(args: Array<String>) {
    runApplication<VendingMachineApplication>(*args)
    println("\n\t API DOCUMENTATION: 📍 http://localhost:8080/swagger-ui/index.html 🚗")
}
