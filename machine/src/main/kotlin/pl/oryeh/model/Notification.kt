package pl.oryeh.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table


@Table @Entity
data class Notification(
    @Id @GeneratedValue(strategy = IDENTITY)
    val id       : Long = 0,
    var text     : String,
    var addInfo  : String? = null,
    val read     : Boolean = false
)  {
    @ManyToOne(fetch = LAZY) @JoinColumn(name = "user_id") @JsonIgnore
    var user: User? = null
}