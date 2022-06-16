package de.steklopod.controller

import de.steklopod.config.security.util.setAccessToken
import de.steklopod.model.TokenInfo
import de.steklopod.model.UserPrincipal
import de.steklopod.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@Validated
@RestController
@RequestMapping(path = ["/user"])
class UserController(private val userService: UserService) {

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "👉🏻 User registration.")
    fun registerNewUser(@RequestBody @AuthenticationPrincipal principal: UserPrincipal, response: HttpServletResponse)
            : TokenInfo {
        userService.notExistsOrThrow(principal.email.trim().toLowerCase())
        val user = userService.createUser(principal)
        log.info("🦹🏼 Finished User registration for: $principal")
        val accessToken = user.makeJwtToken()
        return response setAccessToken accessToken
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
