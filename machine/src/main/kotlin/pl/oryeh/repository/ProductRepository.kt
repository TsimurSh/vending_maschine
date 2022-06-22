package pl.oryeh.repository

import pl.oryeh.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findProductById(id: Long): Product?

    @Modifying
    @Query("UPDATE Product SET cost = :cost WHERE id =:id AND user.id = :sellerId")
    fun updateCost(id: Long, cost: Int, sellerId: Int): Any

    @Modifying
    @Query("UPDATE Product SET amountAvailable = amountAvailable - :amountNew WHERE id =:id AND user.id = :sellerId")
    fun updateAmount(id: Long, amountNew: Int, sellerId: Int)

    @Modifying
    @Query("DELETE FROM Product WHERE id =:id AND user.id = :sellerId")
    fun delete(id: Long, sellerId: Int)
}
