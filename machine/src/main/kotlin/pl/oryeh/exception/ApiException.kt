package pl.oryeh.exception

import org.springframework.http.HttpStatus.*


sealed class ApiException : RuntimeException() {
    data class TokenValidationException(override val message: String? = UNAUTHORIZED.name) : ApiException()
    data class ValidationException(override val message: String? = BAD_REQUEST.name) : ApiException()
    data class NotFoundException(override val message: String? = NOT_FOUND.name) : ApiException()
    data class NotAllowedException(override val message: String = METHOD_NOT_ALLOWED.name) : ApiException()
    data class SoldOutException(override val message: String? = GONE.name) : ApiException()
    data class NotEnoughMoneyException(override val message: String? = PAYMENT_REQUIRED.name) : ApiException()
}
