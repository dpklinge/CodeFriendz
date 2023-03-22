package com.codefriendz.services

import com.codefriendz.errors.InformationNotFoundError
import com.codefriendz.errors.validation.*
import com.codefriendz.models.registration.RegistrationInput
import com.codefriendz.models.registration.RegistrationOutcome
import com.codefriendz.models.toStandardizedPhoneNumberFormat
import com.codefriendz.models.user.CodeFriendzAppUser
import com.codefriendz.repositories.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.awaitFirstOrNull
import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegistrationService(private val userRepository: UserRepository, private val lookupService: UserLookupService, private val passwordEncoder: PasswordEncoder) {
    private val logger = KotlinLogging.logger { }
    suspend fun registerUser(user: RegistrationInput): RegistrationOutcome = coroutineScope {
        logger.debug { "Attempting to register user" }
        var didSucceed = true
        val errorList = mutableListOf<ValidationError>()
        val byEmailDeferred = async { lookupService.lookupByEmail(user.email) }
        val byPhoneNumberDeferred = async { lookupService.lookupByPhone(user.phoneNumber.toStandardizedPhoneNumberFormat()) }
        byEmailDeferred.await()
            .fold(
                {
                    if (it !is InformationNotFoundError) {
                        didSucceed = false
                        errorList.add(ServerError())
                    }
                },
                {
                    didSucceed = false
                    errorList.add(EmailAlreadyExistsError())
                }
            )
        byPhoneNumberDeferred.await().fold(
            {
                if (it !is InformationNotFoundError) {
                    didSucceed = false
                    errorList.add(ServerError())
                }
            },
            {
                didSucceed = false
                errorList.add(PhoneNumberAlreadyExistsError())
            }
        )
        if (user.password != user.passwordConfirmation) {
            didSucceed = false
            errorList.add(NonMatchingPasswordsError())
        }
        var storedUser: CodeFriendzAppUser? = null
        if (didSucceed) {
            logger.debug { "Attempting to store user: $user" }
            storedUser = storeUser(user).awaitFirstOrNull()
            logger.info { "Stored new user: $storedUser" }
        }
        return@coroutineScope RegistrationOutcome(didSucceed, errorList, storedUser?.toReturnUser())
    }

    private suspend fun storeUser(appUser: RegistrationInput) = userRepository.save(CodeFriendzAppUser(appUser.displayName, passwordEncoder.encode(appUser.password), appUser.email, appUser.phoneNumber.toStandardizedPhoneNumberFormat()))
}
