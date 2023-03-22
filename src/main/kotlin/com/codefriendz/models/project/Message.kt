package com.codefriendz.models.project

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.UUID


data class Message(
    @Id
    val threadId: UUID,
    val authorId: UUID,
    val contents: String,
    val sentAt: LocalDateTime,
    val isEdited: Boolean = false
){
    suspend fun updateMessage(newContents: String) = Message(threadId, authorId, newContents, sentAt, true)
}