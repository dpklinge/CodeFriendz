package com.codefriendz.services

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import com.codefriendz.errors.AppServerError
import com.codefriendz.errors.ProjectNameExistsError
import com.codefriendz.errors.UnauthorizedActionError
import com.codefriendz.errors.exceptions.ProjectNameExistsException
import com.codefriendz.models.project.Message
import com.codefriendz.models.project.Project
import com.codefriendz.models.project.ProjectAuthorization
import com.codefriendz.models.user.CodeFriendzAppUser
import com.codefriendz.repositories.ProjectRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val databaseErrorHandler: DatabaseErrorHandler,
    private val messageService: MessageService
) {
    suspend fun createProject(project: Project) = Either.catch {
        if (projectRepository.existsById(project.name).awaitSingle()) {
            ProjectNameExistsException("That project name is already taken!")
        } else {
            projectRepository.save(project)
        }
    }.mapLeft {
        databaseErrorHandler.handleException(it)
    }

    suspend fun addMessageToProject(message: Message, projectName: String, userId: UUID) = either<AppServerError, Message> {
        val proj = projectRepository.findByName(projectName)
        ensure(proj.allowUnauthorizedComments || proj.userAuthorizations[userId]?.canMakeComment() == true){
            UnauthorizedActionError("You are not authorized to make comments on this project.")
        }
        messageService.storeMessage(message).bind()
    }

    suspend fun requestToJoinProject(projectName: String, user: CodeFriendzAppUser, message: String ){
        //TODO
    }
}
