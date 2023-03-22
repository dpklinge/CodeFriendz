package com.codefriendz.services

import arrow.core.Either
import com.codefriendz.models.project.Project
import com.codefriendz.repositories.ProjectRepository
import org.springframework.stereotype.Service

@Service
class ProjectService(private val projectRepository: ProjectRepository, private val databaseErrorHandler: DatabaseErrorHandler) {
    suspend fun createProject(project: Project) = Either.catch{
        projectRepository.save(project)
    }.mapLeft{
        databaseErrorHandler.handleException(it)
    }
}