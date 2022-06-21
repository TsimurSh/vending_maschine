package pl.oryeh.model

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserPrincipal(
    @Email
    var email: String,
    @Length(min = 6, max = 255) @NotBlank
    val password: String,
    val role: Scope
)
