package pl.oryeh.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Table @Entity
data class Role(@Id @Enumerated(STRING) val role: Scope): GrantedAuthority {
    override fun getAuthority(): String = role.name

    @ManyToMany(mappedBy = "roles") @JsonIgnore
    var users: MutableSet<User> = mutableSetOf()
}
