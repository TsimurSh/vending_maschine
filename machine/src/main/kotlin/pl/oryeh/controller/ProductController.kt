package pl.oryeh.controller

import pl.oryeh.config.security.util.userId
import pl.oryeh.model.Product
import pl.oryeh.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping("/product")
class ProductController(private val service: ProductService) {

    @GetMapping @Operation(summary = "Find all products")
    fun findAll() = service.findAll()

    @Secured("SELLER")
    @ResponseStatus(code = CREATED)
    @PostMapping("/{productName}")
    @Operation(summary = "Create product")
    fun create(@PathVariable productName: String, @RequestBody cost: Int, request: HttpServletRequest): Product {
        val product = Product(productName = productName, cost = cost)
        return service.create(product, request.userId())
    }

    @Secured("SELLER")
    @PatchMapping("/cost/{id}")
    @Operation(summary = "Update product cost")
    fun updateCost(@PathVariable id: Long, @RequestBody costNew: Int, request: HttpServletRequest) {
        service.updateCost(productId = id, cost = costNew, sellerId = request.userId())
    }

    @Secured("SELLER")
    @PatchMapping("/amount/{id}")
    @Operation(summary = "Update product amount")
    fun updateAmount(@PathVariable id: Long, @Valid @RequestBody @Min(0) amountNew: Int, request: HttpServletRequest) {
        service.updateAmount(productId = id, amountNew = amountNew, sellerId = request.userId())
    }

    @Secured("SELLER")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    fun deleteProduct(@PathVariable id: Long,  request: HttpServletRequest) {
        service.delete(productId = id, sellerId = request.userId())
    }

}
