package pl.oryeh.service

import pl.oryeh.exception.ApiException.NotEnoughMoneyException
import pl.oryeh.model.Change
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MoneyService(private val userService: UserService, private val productService: ProductService) {

    @Transactional
    fun buyProducts(userId: Int, productId: Long, qty: Int): Change {
        val product = productService.findAndValidate(productId)
        val user = userService.findOrThrow(userId)
        val costTotal = product.cost * qty
        val change = user.deposit!! - costTotal
        if (change < 0) throw NotEnoughMoneyException("💰 User $userId added money (Not enough money for product $product)")
        else {
            productService.updateAmount(productId, -qty, userId)
            userService.decreaseDeposit(user.id, costTotal)
            log.info("💰 User $userId fully payed for [$qty] products $product. DONE")
        }
        return Change(costTotal = costTotal, changeCoins = calculateCoins(change))
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)

        fun calculateCoins(change: Int): List<MutableList<Int>> {
            var accumulator: Int = change
            val hundred = mutableListOf<Int>()
            val fifty = mutableListOf<Int>()
            val twenty = mutableListOf<Int>()
            val dimes = mutableListOf<Int>()
            val nickels = mutableListOf<Int>()
            while (accumulator >= 100) {
                hundred += 100
                accumulator -= 100
            }
            while (accumulator >= 50) {
                fifty += 50
                accumulator -= 50
            }
            while (accumulator >= 20) {
                twenty += 20
                accumulator -= 20
            }
            while (accumulator >= 10) {
                dimes += 10
                accumulator -= 10
            }
            while (accumulator >= 5) {
                nickels += 5
                accumulator -= 5
            }
            log.info("Change coins: " +
                    "🪙 ${hundred.size} hundred coins (100), " +
                    "🪙 ${fifty.size} fifty coins (50), " +
                    "🪙 ${twenty.size} quarters coins (20), " +
                    "${dimes.size} dimes coins (10), " +
                    "${nickels.size} nickels coins (5)  ")
            return listOf(hundred, fifty, twenty, dimes, nickels).filter { it.isNotEmpty() }
        }
    }

}
