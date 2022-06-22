package pl.oryeh.config.security.util

import pl.oryeh.config.security.util.TokenUtil.ACCESS_TOKEN_KEY
import pl.oryeh.config.security.util.TokenUtil.TOKEN_PREFIX
import pl.oryeh.exception.ApiException.TokenValidationException
import pl.oryeh.model.TokenInfo
import org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseCookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

infix fun HttpServletResponse.setAccessToken(jwtAccessToken: String): TokenInfo {
    val age = 60 * 60 * 24 * 7
    addInCookie(ACCESS_TOKEN_KEY, jwtAccessToken, age)
    setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, "$AUTHORIZATION, $ACCESS_TOKEN_KEY")
    setHeader(ACCESS_TOKEN_KEY, jwtAccessToken.removePrefix(TOKEN_PREFIX))
    return TokenInfo(accessToken = TOKEN_PREFIX + jwtAccessToken)
}

fun HttpServletResponse.addInCookie(key: String, jwtToken: String, age: Int) {
    val tokenWithoutPrefix = jwtToken.removePrefix(TOKEN_PREFIX)
    val cookie = ResponseCookie
        .from(key, tokenWithoutPrefix).maxAge(age.toLong())
        .secure(true)
        .httpOnly(true)
    addHeader(SET_COOKIE, cookie.build().toString())
}

fun HttpServletRequest.userId(): Int = TokenUtil.userId(accessJwtToken())
fun HttpServletRequest.userIdOrNull(): Int? = TokenUtil.userIdOrNull(accessJwtTokenOrNull())

fun HttpServletRequest.accessJwtTokenOrNull(): String? = (
        getHeader(AUTHORIZATION) ?: getCookie(ACCESS_TOKEN_KEY)
        )?.removePrefix(TOKEN_PREFIX)?.let { if (it.length < 10) null else it }

fun HttpServletRequest.accessJwtToken(): String = accessJwtTokenOrNull()
    ?: throw TokenValidationException("[$method] $requestURI \n 'Authorization' not exist in request)")

infix fun HttpServletRequest.getCookie(name: String): String? = cookies?.firstOrNull { it?.name == name }?.value
