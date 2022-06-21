package pl.oryeh.repository

import de.steklopod.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findUserByEmail(email: String?): User?
    fun findUserById(id: Int): User?
    fun existsByEmail(email: String?): Boolean
    @Modifying
    fun deleteByEmail(email: String)

    @Modifying
    @Query("UPDATE User SET deposit = deposit + :deposit WHERE id = :id")
    fun updateDeposit(id: Int, deposit: Int): Int
}
