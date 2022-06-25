package pl.oryeh.config.security

import pl.oryeh.config.security.util.TokenUtil
import pl.oryeh.config.security.util.accessJwtToken
import pl.oryeh.exception.ApiException.TokenValidationException
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED


internal class JwtTokenFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            SecurityContextHolder.getContext().authentication = TokenAuthentication(request.accessJwtToken())
            chain.doFilter(request, response)
        } catch (exception: TokenValidationException) {
            response.status = SC_UNAUTHORIZED; response.sendError(SC_UNAUTHORIZED)
        }
    }
}

internal class TokenAuthentication(private val token: String) : AbstractAuthenticationToken(TokenUtil.getRoles(token)) {
    override fun getCredentials()  = token
    override fun getPrincipal()    = TokenUtil.userId(token)
    override fun getAuthorities()  = TokenUtil.getRoles(token)
    override fun getName()         = "JWT"
    override fun isAuthenticated() = true
}
