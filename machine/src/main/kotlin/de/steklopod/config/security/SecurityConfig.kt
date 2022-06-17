package de.steklopod.config.security

import de.steklopod.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_FORBIDDEN


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(private val userService: UserService) : WebSecurityConfigurerAdapter() {
    companion object {
        private val whitelist = arrayOf("/user", "/product", "/actuator/**",
            "/docs/**", "/docs.yaml", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/favicon.ico"
        )
    }

    override fun configure(http: HttpSecurity) {
        http {
            cors { }
            csrf { disable() }; httpBasic { disable() }
            exceptionHandling { authenticationEntryPoint = AuthenticationExceptionHandler() }
            sessionManagement { sessionCreationPolicy = STATELESS }
            authorizeRequests { authorize(anyRequest, authenticated) }
            addFilterAt<UsernamePasswordAuthenticationFilter>(JwtTokenFilter())
            addFilterAt<UsernamePasswordAuthenticationFilter>(LoginFilter("/login", userService, authenticationManagerBean()))
            logout {
                logoutSuccessHandler = LogoutSuccessHandler()
                deleteCookies("JSESSIONID")
                clearAuthentication = true; invalidateHttpSession = true }
        }
    }
    override fun configure(webSecurity: WebSecurity) { webSecurity.ignoring().antMatchers(*whitelist) }
    override fun configure(auth: AuthenticationManagerBuilder) { auth.authenticationProvider(authProvider()) }
    @Bean override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()
    @Bean fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    @Bean fun authProvider   (): DaoAuthenticationProvider = DaoAuthenticationProvider().apply { setUserDetailsService(userService); setPasswordEncoder(BCryptPasswordEncoder()) }
    @Bean fun grantedAuthorityDefaults(): GrantedAuthorityDefaults = GrantedAuthorityDefaults("")


    class AuthenticationExceptionHandler : AuthenticationEntryPoint {
        override fun commence(request: HttpServletRequest, response: HttpServletResponse, ex: AuthenticationException) {
            System.err.println(request.requestURI + " : " + ex.javaClass.simpleName + " - " + ex.localizedMessage)
            with(response) { status = SC_FORBIDDEN; sendError(SC_FORBIDDEN, ex.message) }
        }
    }

}
