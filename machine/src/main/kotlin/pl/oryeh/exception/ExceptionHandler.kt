package pl.oryeh.exception

import pl.oryeh.exception.ApiException.NotEnoughMoneyException
import pl.oryeh.exception.ApiException.NotFoundException
import pl.oryeh.exception.ApiException.SoldOutException
import pl.oryeh.exception.ApiException.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(code = BAD_REQUEST)
    fun validationExHandler(e: ValidationException): String = "Validation exception: ${e.message}".apply {
        log.error(this)
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(code = NOT_FOUND)
    fun notFoundExceptionHandler(e: NotFoundException): String = "Not found exception: ${e.message}".apply {
        log.error(this)
    }

    @ExceptionHandler(SoldOutException::class)
    @ResponseStatus(code = GONE)
    fun soldOutExceptionHandler(e: SoldOutException): String = "No more product of such type: ${e.message}".apply {
        log.error(this)
    }
    @ExceptionHandler(NotEnoughMoneyException::class)
    @ResponseStatus(code = PAYMENT_REQUIRED)
    fun notEnoughMoneyException(e: NotEnoughMoneyException): String = "Not enough money: ${e.message}".apply {
        log.error(this)
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
