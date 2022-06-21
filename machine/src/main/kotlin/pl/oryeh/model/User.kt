package pl.oryeh.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY
import de.steklopod.config.security.util.TokenUtil.SCOPE_KEY
import de.steklopod.config.security.util.TokenUtil.steklopod
import de.steklopod.config.security.util.TokenUtil.tokenExpiration
import de.steklopod.model.Scope.BUYER
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS512
import org.hibernate.annotations.ColumnDefault
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit.DAYS
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.Email


@Entity @Table
data class User(
    @Id @GeneratedValue(strategy = IDENTITY)
    val id: Int = 0,
    @Email @Column(nullable = false, unique = true)
    val email: String? = null,
    @ColumnDefault("0")
    val deposit: Int? = null,
    @JsonIgnore @ColumnDefault("true")
    val enabled: Boolean? = null,
    @JsonProperty("password", access = WRITE_ONLY)
    private var password: String? = null
) :  UserDetails {

    @ManyToMany @JoinTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "role", referencedColumnName = "role")]) @JsonIgnore
    var roles : MutableSet<Role> = mutableSetOf(Role(BUYER))

    @OneToMany(mappedBy = "user") @JsonIgnore
    var notifications: MutableList<Notification> = mutableListOf()

    @OneToMany(mappedBy = "user") @JsonIgnore
    var products: MutableList<Product> = mutableListOf()

    @OneToMany(mappedBy = "user") @JsonIgnore
    var coins: MutableList<DepositCoin> = mutableListOf()

    var scope : List<String>
        get() = roles.map { it.role.name }
        set(v) { }

    @JsonIgnore override fun getAuthorities() = roles
    @JsonIgnore override fun getUsername() = email
    @JsonIgnore override fun getPassword() = password
    @JsonIgnore override fun isEnabled() = enabled!!
    @JsonIgnore override fun isAccountNonLocked() = enabled!!
    @JsonIgnore override fun isAccountNonExpired() = enabled!!
    @JsonIgnore override fun isCredentialsNonExpired() = enabled!!

    fun withRoles(vararg newRoles: Scope) = apply { newRoles.forEach { roles.add(Role(it)) } }

    fun makeJwtToken(): String = Jwts.builder()
        .claim(SCOPE_KEY, scope.toString())
        .setAudience(username)
        .setSubject(id.toString())
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date(Date().time + DAYS.toMillis(tokenExpiration)))
        .signWith(HS512, steklopod)
        .compact()
}
