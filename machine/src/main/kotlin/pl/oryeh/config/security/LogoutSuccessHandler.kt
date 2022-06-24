package pl.oryeh.config.security


import pl.oryeh.config.ApiConfig.Companion.host
import pl.oryeh.config.security.util.TokenUtil.ACCESS_TOKEN_KEY
import org.springframework.http.HttpHeaders.REFERER
import org.springframework.http.HttpStatus.OK
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


internal class LogoutSuccessHandler : HttpStatusReturningLogoutSuccessHandler(OK) {

    override fun onLogoutSuccess(
        request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?
    ) {
        response.apply {
            listOf(REFERER, ACCESS_TOKEN_KEY).forEach {
                addCookie(Cookie(it, "").apply { maxAge = 0; domain = host; path = "/" })
            }
        }
        super.onLogoutSuccess(request, response, authentication)
    }
}
