package pl.oryeh.service

import de.steklopod.service.MoneyService.Companion.calculateCoins
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MoneyServiceTest {

    @Test
    fun calculateChangeTest() {
        val change = calculateCoins(125)
        with(change) {
            println(this)
            assertEquals(3, size)
            assertEquals(1, first().size)
            assertEquals(100, first().first())
        }
    }
}
