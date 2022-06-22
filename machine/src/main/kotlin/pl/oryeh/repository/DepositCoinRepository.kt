package pl.oryeh.repository

import pl.oryeh.model.DepositCoin
import pl.oryeh.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface DepositCoinRepository : JpaRepository<DepositCoin, Int> {
    @Query("SELECT coin FROM DepositCoin coin WHERE coin.user.id = :userId")
    fun findAllByUserId(userId: Int): List<DepositCoin>

    @Modifying @Query("DELETE FROM DepositCoin coin WHERE coin.user.id = :userId")
    fun deleteAll(userId: Int)

}
