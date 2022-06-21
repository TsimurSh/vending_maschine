package pl.oryeh.repository

import de.steklopod.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserRepositoryTest(@Autowired private val repository: UserRepository) {

    private val userNew: User = User(
        email = "Dmitry_Kaltovich@colaba.online",
        password = "new_password"
    )

    @Test
    fun `Create and delete user test`() {
        val user = repository.save(userNew)
        with(repository.findUserById(user.id)) {
            assertNotNull(this!!)
            assertTrue(authorities.isNotEmpty())
        }
        repository.delete(user)
        assertFalse(repository.existsByEmail(user.email))
    }


}
