package pl.oryeh.repository

import pl.oryeh.model.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findNotificationById(id: Long): Notification?

    fun findAllByUserIdAndReadFalse(userId: Int): List<Notification>

    @Query(
        "INSERT INTO vending.notification(user_id, text, add_info) VALUES (:userId,:text,:link,:addInfo) RETURNING id",
        nativeQuery = true
    )
    fun create(userId: Int, text: String, link: String? = null, addInfo: String? = null): Long

    @Modifying
    @Query("UPDATE Notification SET read=:read WHERE id=:id")
    fun read(id: Long, read: Boolean = true)

    @Modifying
    @Query("UPDATE Notification SET read=:read WHERE id IN(:id)")
    fun read(id: Collection<Long>, read: Boolean = true)

    @Modifying
    @Query("UPDATE Notification SET read=:read WHERE user.id=:userId")
    fun readAll(userId: Int, read: Boolean = true)
}
