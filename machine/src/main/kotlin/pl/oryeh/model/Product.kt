package pl.oryeh.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY
import pl.oryeh.exception.ApiException.ValidationException
import org.hibernate.annotations.ColumnDefault
import org.hibernate.validator.constraints.Length
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.validation.constraints.Min
import javax.validation.constraints.Positive


@Entity
@Table
data class Product(
    @Id @GeneratedValue(strategy = IDENTITY) @JsonProperty(access = READ_ONLY)
    val id: Long = 0,
    @Length(min = 2) @Column(unique = true, nullable = false)
    val productName: String,
    @Min(5) @Column(nullable = false)
    val cost: Int,
    @ColumnDefault("0") @Positive
    val amountAvailable: Int = 0
) {
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") @JsonIgnore
    var user: User? = null

    @PrePersist
    fun prePersist() {
        if (cost % 5 != 0) throw ValidationException("❗️ Should be in multiples of 5")
    }
}
