package com.codefriendz.controllers

import com.codefriendz.errors.AppServerError
import com.codefriendz.models.*
import com.codefriendz.models.registration.RegistrationInput
import com.codefriendz.models.registration.RegistrationOutcome
import com.codefriendz.services.RegistrationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
class RegistrationController(val registrationService: RegistrationService) {

    @Operation(
        summary = "Attempts to register a user with the provided information",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = RegistrationOutcome::class)
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
    @PostMapping("register")
    suspend fun registerUser(
        @Valid @RequestBody
        userRegistration: RegistrationInput
    ) = registrationService.registerUser(userRegistration)
}
