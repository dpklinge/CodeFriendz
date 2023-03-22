package com.codefriendz.services

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import com.codefriendz.errors.AppServerError
import com.codefriendz.errors.InformationNotFoundError
import com.codefriendz.errors.InvalidInputError
import com.codefriendz.errors.exceptions.InformationNotFoundException
import com.codefriendz.models.toStandardizedPhoneNumberFormat
import com.codefriendz.models.user.CodeFriendzAppUser
import com.codefriendz.repositories.UserRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class UserLookupService(private val userRepository: UserRepository, val databaseErrorHandler: DatabaseErrorHandler) {
    private val logger = KotlinLogging.logger {}
    suspend fun lookupByEmail(email: String): Either<AppServerError, CodeFriendzAppUser> = Either.catch {
        userRepository.findAppUserByEmail(email)
            ?: throw InformationNotFoundException(InformationNotFoundError("Email $email not located"))
    }.mapLeft {
        databaseErrorHandler.handleException(it)
    }

    suspend fun lookupByPhone(phoneNumber: String): Either<AppServerError, CodeFriendzAppUser> = Either.catch {
        userRepository.findAppUserByPhoneNumber(phoneNumber) ?: throw InformationNotFoundException(
            InformationNotFoundError("Phone number $phoneNumber not located")
        )
    }.mapLeft {
        databaseErrorHandler.handleException(it)
    }

    suspend fun lookupUsername(username: String): Either<AppServerError, CodeFriendzAppUser> = either {
        try {
            logger.debug { "Looking up username by email" }
            val emailLookup = lookupByEmail(username)
            if (emailLookup.isLeft()) {
                logger.debug { "Email lookup failed, trying phone number" }
                lookupByPhone(username.toStandardizedPhoneNumberFormat()).bind()
            } else {
                logger.debug { "Email lookup succeeded, returning" }
                emailLookup.bind()
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            InvalidInputError(message = "Invalid username or password", inputs = arrayOf(username)).left().bind<CodeFriendzAppUser>()
        }
    }
}
