package pl.oryeh.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
@ConditionalOnProperty("spring.application.develop", havingValue = "true")
class CorsConfig : CorsWebFilter(corsFilter()) {

    companion object {
        fun corsFilter() = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration(
                "/**",
                CorsConfiguration().apply {
                    allowedOriginPatterns = listOf("http://localhost:3000")
                    addAllowedMethod("*")
                    addAllowedHeader("*")
                    allowCredentials = true
                }
            )
        }
    }

}
