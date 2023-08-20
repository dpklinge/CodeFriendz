package com.codefriendz.controllers

import com.codefriendz.errors.AppServerError
import com.codefriendz.models.project.Message
import com.codefriendz.models.project.Project
import com.codefriendz.services.ProjectService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Controller
class ProjectController(private val projectService: ProjectService) {
    @Operation(
        summary = "Attempts to create a new project with provided username and password",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Project::class)
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
    @PostMapping("/project")
    suspend fun createProject(@RequestBody project: Project) =
        projectService.createProject(project).toResponseEntity()

    @Operation(
        summary = "Attempts to create a new message in a project",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Project::class)
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
    @PostMapping("/project/{projectName}/messages")
    suspend fun createMessage(
        @AuthenticationPrincipal principal: Jwt,
        @RequestBody message: Message,
        @PathVariable projectName: String
    ) {
        val id = principal.getClaim<UUID>("userId")
        projectService.addMessageToProject(message, projectName, id).toResponseEntity()
    }
}
