package pl.oryeh.service

import pl.oryeh.exception.ApiException.ValidationException
import pl.oryeh.model.DepositCoin
import pl.oryeh.model.User
import pl.oryeh.repository.DepositCoinRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DepositCoinService(private val repository: DepositCoinRepository,
private val userService: UserService) {

    @Transactional
    fun addCoinToDeposit(coin: Int, userId: Int): DepositCoin {
        val deposit = DepositCoin(coin = coin.validateCoin())
        deposit.user = User(id = userId)
        log.info("💌 New deposit coin to save: $deposit")
        userService.increaseDeposit(userId, deposit.coin)
        return repository.save(deposit)
    }

    @Transactional
    fun reset(userId: Int) = repository.deleteAll(userId)

    fun findAll(userId: Int) = repository.findAllByUserId(userId)


    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private fun Int.validateCoin() =
            apply { if (this !in 0..100 || this % 5 != 0) throw ValidationException("❗️ Coin must be in multiples of 5") }
    }

}

