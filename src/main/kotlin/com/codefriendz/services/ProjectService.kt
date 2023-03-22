package com.codefriendz.services

import arrow.core.Either
import com.codefriendz.errors.ProjectNameExistsError
import com.codefriendz.errors.exceptions.ProjectNameExistsException
import com.codefriendz.models.project.Project
import com.codefriendz.repositories.ProjectRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val databaseErrorHandler: DatabaseErrorHandler
) {
    suspend fun createProject(project: Project) = Either.catch {
        if (projectRepository.existsById(project.name).awaitSingle()) {
            throw ProjectNameExistsException("That project name is already taken!")
        } else {
            projectRepository.save(project)
        }
    }.mapLeft {
        if (it is ProjectNameExistsException) {
            ProjectNameExistsError(it.message.toString())
        } else {
            databaseErrorHandler.handleException(it)
        }
    }
}
