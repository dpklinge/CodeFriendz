package com.codefriendz.controllers

import com.codefriendz.errors.AppServerError
import com.codefriendz.models.project.Message
import com.codefriendz.services.MessageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Controller
class MessageController(private val messageService: MessageService) {
    @Operation(
        summary = "Attempts to send a message to the requested thread",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Message::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AppServerError::class)
                    )
                ]
            )
        ]
    )
    @PostMapping("/sendMessage")
    suspend fun createMessage(@RequestBody message: Message) =
        messageService.storeMessage(message).toResponseEntity()

    @Operation(
        summary = "Gets a list of messages for the requested thread",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = Message::class))
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AppServerError::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/getMessage")
    suspend fun getMessages(@RequestParam id: UUID, @RequestParam page: Int, @RequestParam amount: Int) =
        messageService.getMessagesForThread(id, page, amount)
}
