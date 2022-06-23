package pl.oryeh.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY
import pl.oryeh.exception.ApiException.ValidationException
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.FetchType.LAZY
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.validation.constraints.Positive


@Entity
@Table
data class DepositCoin(
    @Id @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,
    @Positive @Column(nullable = false)
    val coin: Int
) {
    @ManyToOne(fetch = LAZY) @JoinColumn(name = "user_id") @JsonIgnore
    var user: User? = null

    @PrePersist
    fun prePersist() {
        if (coin !in 0..100 || coin % 5 != 0) throw ValidationException("❗️ Should be in multiples of 5")
    }
}
