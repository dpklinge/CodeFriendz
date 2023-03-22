package com.codefriendz.repositories

import com.codefriendz.models.project.Message
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.*

interface MessageRepository : R2dbcRepository<Message, UUID> {
    fun findAllByThreadId(threadId: UUID, pageRequest: Pageable): Flow<Message>
    suspend fun existsByThreadIdAndSentAt(threadId: UUID, sentAt: LocalDateTime): Boolean
    @Modifying
    @Query("INSERT INTO Message (thread_id, author_id, contents, sent_at, is_edited) VALUES (:#{#message.threadId}, :#{#message.authorId}, :#{#message.contents}, :#{#message.sentAt}, :#{#message.isEdited})")
    suspend fun insertMessage(message: Message): Int
    @Modifying
    @Query("UPDATE Message SET contents = :#{#message.contents}, is_edited = true WHERE thread_id = :#{#message.threadId} AND sent_at = :#{#message.sentAt}")
    suspend fun updateMessage(message: Message): Int
}