package com.codefriendz.services

import com.codefriendz.FlywayTestConfig
import com.codefriendz.models.project.Message
import com.codefriendz.models.registration.RegistrationInput
import com.codefriendz.models.toAppStandardLocalDateTime
import com.codefriendz.models.user.AppUserNoPassword
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.time.LocalDateTime
import java.util.*
import javax.annotation.PostConstruct

@SpringBootTest
@Import(FlywayTestConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageIntegrationTests {
    @Autowired
    private lateinit var messageService: MessageService
    @Autowired
    private lateinit var registrationService: RegistrationService
    private lateinit var user: AppUserNoPassword
    @PostConstruct
    fun setup() {
        println("Executing postconstruct")
        runBlocking {
            user = registrationService.registerUser(
                RegistrationInput(
                    "Test user",
                    "test password",
                    "test password",
                    "email@email.com",
                    "9801111111"
                )
            ).registeredUser!!
        }
    }

    @Test
    fun `message saves and reads correctly`() {
        val threadId = UUID.randomUUID()

        runBlocking {
            val message = Message(threadId, user.id, "Test message", LocalDateTime.now().toAppStandardLocalDateTime())
            messageService.storeMessage(message)

            val read = messageService.getMessagesForThread(threadId)
            read.fold(
                { fail(it.message) },
                {
                    val results = it.toList()
                    assertEquals(message.contents, results[0].contents)
                    assertEquals(message.threadId, results[0].threadId)
                    assertEquals(message.authorId, results[0].authorId)
                    assertEquals(message.sentAt, results[0].sentAt)
                    assertEquals(false, results[0].isEdited)
                }
            )
        }
    }

    @Test
    fun `message saves and edits correctly`() {
        val threadId = UUID.randomUUID()

        runBlocking {
            val message = Message(threadId, user.id, "Test message", LocalDateTime.now().toAppStandardLocalDateTime())
            val editedContents = message.contents + " edited"
            messageService.storeMessage(message)
            messageService.storeMessage(message.updateMessage(editedContents))

            val read = messageService.getMessagesForThread(threadId)

            read.fold(
                { fail(it.message) },
                {
                    val results = it.toList()
                    assertEquals(editedContents, results[0].contents)
                    assertEquals(message.threadId, results[0].threadId)
                    assertEquals(message.authorId, results[0].authorId)
                    assertEquals(message.sentAt, results[0].sentAt)
                    assertEquals(true, results[0].isEdited)
                }
            )
        }
    }

    @Test
    fun `message read pagination works correctly`() {
        val threadId = UUID.randomUUID()

        runBlocking {
            for (i in 30 downTo 1) {
                val message = Message(threadId, user.id, "Test message", LocalDateTime.now().minusMinutes(i.toLong()).toAppStandardLocalDateTime())
                messageService.storeMessage(message)
            }

            val read = messageService.getMessagesForThread(threadId, 0, 20)
            read.fold(
                { fail(it.message) },
                {
                    val results = it.toList()
                    assertEquals(20, results.size)
                }
            )
            val read2 = messageService.getMessagesForThread(threadId, 1, 20)
            read2.fold(
                { fail(it.message) },
                {
                    val results = it.toList()
                    assertEquals(10, results.size)
                }
            )
        }
    }
}
