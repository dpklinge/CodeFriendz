package com.codefriendz.controllers

import com.codefriendz.errors.AppServerError
import com.codefriendz.models.project.Project
import com.codefriendz.services.ProjectService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class ProjectController(private val projectService: ProjectService) {
    @Operation(
        summary = "Attempts to create a new project in with provided username and password",
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
    @PostMapping("/createProject")
    suspend fun createProject(@RequestBody project: Project) =
        projectService.createProject(project).toResponseEntity()
}
