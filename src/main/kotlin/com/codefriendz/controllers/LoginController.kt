package com.codefriendz.controllers

import com.codefriendz.errors.AppServerError
import com.codefriendz.models.user.AppUserNoPassword
import com.codefriendz.services.LoginService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(val loginService: LoginService) {

    @GetMapping("test")
    fun test() = "test"

    @Operation(
        summary = "Attempts to log in with provided username and password",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AppUserNoPassword::class)
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
    @PostMapping("/login")
    suspend fun loginUser(@RequestBody loginPacket: LoginPacket) =
        loginService.verifyLoginInfo(loginPacket.username, loginPacket.password).toResponseEntity()
}

data class LoginPacket(val username: String, val password: String)
