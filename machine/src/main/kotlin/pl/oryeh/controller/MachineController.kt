package pl.oryeh.controller

import pl.oryeh.config.security.util.userId
import pl.oryeh.service.DepositCoinService
import pl.oryeh.service.MoneyService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.Positive

@Secured("BUYER")
@Validated
@RestController
@RequestMapping("/")
class MachineController(private val moneyService: MoneyService,
                        private val depositCoinService: DepositCoinService) {

    @PostMapping("/deposit")
    @Operation(summary = "Deposit only 5, 10, 20, 50 and 100 cent coins into their vending machine account (one coin at the time)")
    fun deposit( @RequestBody @Valid @Positive amount: Int, request: HttpServletRequest) =
        depositCoinService.addCoinToDeposit(userId = request.userId(), coin = amount)


    @PostMapping("/buy/{productId}")
    @Operation(summary = "Buy a product with the money they’ve deposited. API should return total they’ve spent, the product they’ve purchased and their change if there’s any (in an array of 5, 10, 20, 50 and 100 cent coins)")
    fun buy(
        @RequestBody @Positive qty: Int = 1,
        @PathVariable productId: Long,
        request: HttpServletRequest
    ) = moneyService.buyProducts(request.userId(), productId, qty)


    @DeleteMapping("/reset")
    @Operation(summary = "Reset deposit back to 0")
    fun reset(request: HttpServletRequest) = depositCoinService.reset(request.userId())

}
