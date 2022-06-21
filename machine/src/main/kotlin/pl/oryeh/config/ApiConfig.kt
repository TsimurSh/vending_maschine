package pl.oryeh.config

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.PostConstruct

@Configuration
@ConfigurationProperties("api")
class ApiConfig : WebMvcConfigurer {
    lateinit var corsAllowed: String
    lateinit var domain: String

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
            .allowedOriginPatterns(corsAllowed, "http://localhost:3000")
    }

    @PostConstruct fun init() {
        host = domain
    }

    @Bean fun hibernate5Module(): Module = Hibernate5Module()

    companion object {
        var host = "pl.oryeh"
    }
}
