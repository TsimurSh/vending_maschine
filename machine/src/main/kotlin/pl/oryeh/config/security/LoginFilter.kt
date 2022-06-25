package pl.oryeh.config.security

import pl.oryeh.config.security.util.setAccessToken
import pl.oryeh.model.TokenInfo
import pl.oryeh.model.User
import pl.oryeh.model.UserPrincipal
import pl.oryeh.service.UserService
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder.getContext
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.validation.annotation.Validated
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Validated
internal class LoginFilter(
    securityRelativePath: String,
    private val userService: UserService,
    private val authManager: AuthenticationManager,
) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(securityRelativePath)) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private val objectMapper = jacksonObjectMapper().registerModule(KotlinModule(nullIsSameAsDefault = true))

        fun User.toAuthentication(password: String = this.password!!): Authentication =
            UsernamePasswordAuthenticationToken(username, password, authorities)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val userPrincipal: UserPrincipal = objectMapper.readValue(request.inputStream, UserPrincipal::class.java)
        log.debug("${userPrincipal.email} trying to login")

        val user = userService.findOrThrow(userPrincipal.email).copy(password = userPrincipal.password)

        val authentication = user.toAuthentication()
        getContext().authentication = authManager.authenticate(authentication)
        log.info("OK user [${userPrincipal.email}] login")
        return getContext().authentication
    }


    override fun successfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, auth: Authentication
    ) {
        val user = auth.principal as User
        with(response) {
            val tokenInfo: TokenInfo = this setAccessToken (user.makeJwtToken())
            writer.write(
                objectMapper.writeValueAsString(tokenInfo)
            )
        }
    }

}
