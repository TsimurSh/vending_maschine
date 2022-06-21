package pl.oryeh.config.security.util

import de.steklopod.config.security.util.TokenUtil.TokenExceptionCode.EXPIRED_TOKEN
import de.steklopod.config.security.util.TokenUtil.TokenExceptionCode.INVALID_TOKEN
import de.steklopod.config.security.util.TokenUtil.TokenExceptionCode.NOT_ACCESS_TOKEN
import de.steklopod.config.security.util.TokenUtil.TokenExceptionCode.TOKEN_CLAIMS_STRING_IS_EMPTY
import de.steklopod.config.security.util.TokenUtil.TokenExceptionCode.UNSUPPORTED_TOKEN
import de.steklopod.exception.ApiException.TokenValidationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.security.core.authority.SimpleGrantedAuthority


object TokenUtil {
    const val tokenExpiration = 13L
    const val SCOPE_KEY = "scope"
    const val steklopod = "steklopod"
    const val TOKEN_PREFIX = "Bearer "
    const val ACCESS_TOKEN_KEY = "access_token"

    fun userIdOrNull(token: String?): Int? = token?.let { userId(token) }
    fun userId(token: String): Int = try {
        getSubject(token).toInt()
    } catch (e: NumberFormatException) {
        System.err.println("SUBJECT in token: [${audience(token)} is not valid user id (Int expected)]")
        throw TokenValidationException(NOT_ACCESS_TOKEN.name)
    }

    fun getRoles(token: String): MutableSet<SimpleGrantedAuthority> {
        val scopesString = getBody(token)[SCOPE_KEY] as String?
        val scopes = scopesString?.removeSurrounding("[", "]")
            ?.split(",")?.map { SimpleGrantedAuthority(it.trim()) }?.toMutableSet()
        return scopes ?: mutableSetOf(SimpleGrantedAuthority("GUEST"))
    }

    private fun getBody(token: String): Claims = parseClaims(token).body

    private fun getSubject(token: String): String = getBody(token).subject

    private fun audience(token: String): String = getBody(token).audience

    private fun parseClaims(bearerToken: String): Jws<Claims> {
        try {
            val tokenOnly = bearerToken.removePrefix(TOKEN_PREFIX)
            return Jwts.parser().setSigningKey(steklopod).parseClaimsJws(tokenOnly)
        } catch (e: Exception) {
            val code: TokenExceptionCode = when (e) {
                is SignatureException -> EXPIRED_TOKEN
                is ExpiredJwtException -> EXPIRED_TOKEN
                is MalformedJwtException -> INVALID_TOKEN
                is UnsupportedJwtException -> UNSUPPORTED_TOKEN
                is IllegalArgumentException -> TOKEN_CLAIMS_STRING_IS_EMPTY
                else -> throw e
            }
            throw TokenValidationException(code.name)
        }
    }

    enum class TokenExceptionCode {
        INVALID_TOKEN,
        EXPIRED_TOKEN,
        NOT_ACCESS_TOKEN,
        UNSUPPORTED_TOKEN,
        TOKEN_CLAIMS_STRING_IS_EMPTY,
        ;
    }

}
