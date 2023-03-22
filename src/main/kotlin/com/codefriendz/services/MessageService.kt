package com.codefriendz.services

import arrow.core.Either
import com.codefriendz.models.project.Message
import com.codefriendz.repositories.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessageService(val messageRepository: MessageRepository, val databaseErrorHandler: DatabaseErrorHandler) {
    suspend fun getMessagesForThread(id: UUID, page: Int = 0, amount: Int = 20) = Either.catch{
        messageRepository.findAllByThreadId(id, PageRequest.of(page, amount, Sort.by(Sort.Direction.DESC, "sentAt")))
    }.mapLeft { databaseErrorHandler.handleException(it)  }

    suspend fun storeMessage(message: Message) = Either.catch{
        val result = if(messageRepository.existsByThreadIdAndSentAt(message.threadId, message.sentAt)){
            messageRepository.updateMessage(message)
        }else{
            messageRepository.insertMessage(message)
        }
        CoroutineScope(Dispatchers.IO).launch {
            notifyThreadFollowers(message.threadId)
        }
        result
    }.mapLeft { databaseErrorHandler.handleException(it)  }

    private suspend fun notifyThreadFollowers(id: UUID) {
        //TODO notifications
    }
}