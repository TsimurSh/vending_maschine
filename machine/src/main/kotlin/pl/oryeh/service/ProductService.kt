package pl.oryeh.service

import pl.oryeh.exception.ApiException.NotFoundException
import pl.oryeh.exception.ApiException.SoldOutException
import pl.oryeh.exception.ApiException.ValidationException
import pl.oryeh.model.Product
import pl.oryeh.model.User
import pl.oryeh.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.Sort.by
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(private val repository: ProductRepository) {
    fun findAll(sort: Sort? = null): List<Product> = repository.findAll(
        sort ?: by(DESC, "cost")
    )
    @Cacheable("product", key = "#id")
    fun find(id: Long): Product? = repository.findProductById(id)

    fun findOrThrow(id: Long): Product = find(id) ?: throw NotFoundException("🏮 Not Found! Product id: `$id`")

    fun findAndValidate(id: Long): Product {
        val product = findOrThrow(id)
        if (product.amountAvailable < 1) throw SoldOutException("Product $id is sold out")
        return product
    }

    @Transactional
    @CacheEvict("product", key = "#product.id")
    fun create(product: Product, userId: Int): Product {
        product.user = User(id = userId)
        log.info("💌 New product to save: $product")
        return repository.save(product)
    }

    @Transactional
    @CacheEvict("product", key = "#productId")
    fun delete(productId: Long, sellerId: Int) = repository.delete(productId, sellerId)

    @Transactional
    @CacheEvict("product", key = "#productId")
    fun updateCost(productId: Long, cost: Int, sellerId: Int) =
        repository.updateCost(productId, cost.validateCost(), sellerId)

    @Transactional
    @CacheEvict("product", key = "#productId")
    fun updateAmount(productId: Long, amountNew: Int, sellerId: Int) =
        repository.updateAmount(productId, amountNew, sellerId)


    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private fun Int.validateCost() = apply { if (this !in 0..100 || this % 5 != 0) throw ValidationException("❗️ Cost must be in multiples of 5") }
    }

}

