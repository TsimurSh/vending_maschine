package de.steklopod.service

import de.steklopod.exception.ApiException.NotFoundException
import de.steklopod.exception.ApiException.ValidationException
import de.steklopod.model.User
import de.steklopod.model.UserPrincipal
import de.steklopod.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val repository: UserRepository): UserDetailsManager {
    override fun loadUserByUsername(email: String?): User? = repository.findUserByEmail(email?.lowercase()?.trim())
    override fun changePassword(oldPassword: String, newPassword: String) = throw NotImplementedError()
    override fun deleteUser(email: String) { log.info("🤷 Deleting user by email: $email"); repository.deleteByEmail(email.toLowerCase()) }
    override fun updateUser(userDetails: UserDetails) { repository.save(userDetails as User) }
    override fun userExists(email: String?): Boolean = repository.existsByEmail(email?.lowercase()?.trim())
    override fun createUser(userDetails: UserDetails) {
        notExistsOrThrow(userDetails.username)
        val user = repository.save(User(email = userDetails.username, password = userDetails.password.encodePassword(), deposit = 0))
        log.info("👨🏻 User saved with scope: ${user.scope}")
    }

    fun notExistsOrThrow(email: String) { if (userExists(email)) throw RuntimeException("USER_EXIST with email $email") }

    fun findOrThrow(email: String): User = loadUserByUsername(email) ?: throw NotFoundException("User not found: $email")
    fun findOrThrow(id: Int): User  = repository.findUserById(id)?: throw NotFoundException("User with id: $id")

    fun createUser(userDto: UserPrincipal): User {
        val user = User(email = userDto.email.toLowerCase(), password = userDto.password.encodePassword(), deposit = 0)
            .withRoles(userDto.role)
        log.info("👳🏼🧑🏻‍💻 saved new user: $user")
        return repository.save(user)
    }

    @Transactional
    fun delete(userId: Int) = repository.deleteById(userId)

    @Transactional
    fun decreaseDeposit(userId: Int, deposit: Int) {
        val wasUpdated = repository.updateDeposit(id = userId, deposit = -deposit) > 0
        if (wasUpdated) log.info("💰 Deposit decreased by -$deposit")
        else throw NotFoundException("💰 User $userId does not exist ")
    }

    @Transactional
    fun increaseDeposit(userId: Int, deposit: Int) {
        val wasUpdated = repository.updateDeposit(id = userId, deposit = deposit.validateDeposit()) > 0
        if (wasUpdated) log.info("💰 Deposit increased by $deposit")
        else throw NotFoundException("💰 User $userId does not exist ")
    }

    companion object {
        private fun String?.encodePassword(): String = BCryptPasswordEncoder().encode(this)
        private fun Int.validateDeposit() = apply { if (this !in 0..100 || this % 5 != 0) throw ValidationException("❗️ Deposit must be in multiples of 5") }
        private val log = LoggerFactory.getLogger(this::class.java)
    }

}
