package pl.oryeh.service

import de.steklopod.model.Scope
import de.steklopod.model.UserPrincipal
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserServiceTest(@Autowired private val service: UserService) {

    private val userNew: UserPrincipal = UserPrincipal(
        email = "Dmitry_Kaltovich@colaba.online", password = "new_password", role = Scope.BUYER
    )

    @Test
    fun updateDepositTest() {
        val user = service.createUser(userNew)
        with(service.findOrThrow(user.id)) {
            assertTrue(authorities.isNotEmpty())
        }
        //update Deposit test
        service.increaseDeposit(user.id, 5)
        val userAfterUpdate = service.findOrThrow(user.id)
        assertEquals(
            5,
            userAfterUpdate.deposit!! - user.deposit!!
        )

        service.delete(user.id)
        assertFalse(service.userExists(user.email))
    }
}
