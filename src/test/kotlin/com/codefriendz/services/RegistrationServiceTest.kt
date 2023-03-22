package com.codefriendz.services

import arrow.core.left
import arrow.core.right
import com.codefriendz.errors.InformationNotFoundError
import com.codefriendz.errors.validation.EmailAlreadyExistsError
import com.codefriendz.errors.validation.NonMatchingPasswordsError
import com.codefriendz.errors.validation.PhoneNumberAlreadyExistsError
import com.codefriendz.models.registration.RegistrationInput
import com.codefriendz.models.toStandardizedPhoneNumberFormat
import com.codefriendz.models.user.CodeFriendzAppUser
import com.codefriendz.repositories.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import java.util.UUID

class RegistrationServiceTest {
    private lateinit var registrationService: RegistrationService

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var lookupService: UserLookupService

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        registrationService = RegistrationService(userRepository, lookupService, passwordEncoder)
    }

    @Test
    fun `registration fails when passwords don't match`() {
        runBlocking {
            val input = generateRegistrationInput(passwordConfirmation = ("Wrong confirmation"))
            whenever(lookupService.lookupByEmail(input.email)).thenReturn(InformationNotFoundError().left())
            whenever(lookupService.lookupByPhone(input.phoneNumber.toStandardizedPhoneNumberFormat())).thenReturn(InformationNotFoundError().left())
            val output = registrationService.registerUser(input)
            assertTrue(output.errors[0] is NonMatchingPasswordsError)
            assertFalse(output.didSucceed)
            verifyNoInteractions(userRepository)
        }
    }

    @Test
    fun `registration fails when phonenumber already exists`() {
        runBlocking {
            val input = generateRegistrationInput()
            whenever(lookupService.lookupByEmail(input.email)).thenReturn(InformationNotFoundError().left())
            whenever(lookupService.lookupByPhone(input.phoneNumber.toStandardizedPhoneNumberFormat())).thenReturn(generateAppUser().right())

            val output = registrationService.registerUser(input)
            assertTrue(output.errors[0] is PhoneNumberAlreadyExistsError)
            assertFalse(output.didSucceed)
            verifyNoInteractions(userRepository)
        }
    }

    @Test
    fun `registration fails when email already exists`() {
        runBlocking {
            val input = generateRegistrationInput()
            whenever(lookupService.lookupByPhone(input.phoneNumber.toStandardizedPhoneNumberFormat())).thenReturn(InformationNotFoundError().left())
            whenever(lookupService.lookupByEmail(input.email)).thenReturn(generateAppUser().right())

            val output = registrationService.registerUser(input)
            assertTrue(output.errors[0] is EmailAlreadyExistsError)
            assertFalse(output.didSucceed)
            verifyNoInteractions(userRepository)
        }
    }

    @Test
    fun `registration fails when phone number and email already exist`() {
        runBlocking {
            val input = generateRegistrationInput()
            val user = generateAppUser()
            whenever(lookupService.lookupByPhone(input.phoneNumber.toStandardizedPhoneNumberFormat())).thenReturn(user.right())
            whenever(lookupService.lookupByEmail(input.email)).thenReturn(user.right())

            val output = registrationService.registerUser(input)
            assertTrue(output.errors.size == 2)
            assertFalse(output.didSucceed)
            verifyNoInteractions(userRepository)
        }
    }

    @Test
    fun `registration succeeds with valid info`() {
        runBlocking {
            val input = generateRegistrationInput()
            val appUser = CodeFriendzAppUser(input.displayName, input.password, input.email, input.phoneNumber.toStandardizedPhoneNumberFormat())
            val appUserReturn = CodeFriendzAppUser(input.displayName, input.password, input.email, input.phoneNumber.toStandardizedPhoneNumberFormat(), UUID.randomUUID())
            whenever(lookupService.lookupByEmail(input.email)).thenReturn(InformationNotFoundError().left())
            whenever(lookupService.lookupByPhone(input.phoneNumber.toStandardizedPhoneNumberFormat())).thenReturn(InformationNotFoundError().left())
            whenever(passwordEncoder.encode(input.password)).thenReturn(input.password)
            whenever(userRepository.save(appUser)).thenReturn(Mono.just(appUserReturn))

            val output = registrationService.registerUser(input)

            assertTrue(output.errors.isEmpty())
            assertTrue(output.didSucceed)
            verify(userRepository).save(any())
        }
    }

    fun generateRegistrationInput(
        displayName: String = ("Test display name"),
        password: String = ("Test password"),
        passwordConfirmation: String = ("Test password"),
        email: String = ("test@email.com"),
        phoneNumber: String = ("(980)-111-1111")
    ) = RegistrationInput(displayName, password, passwordConfirmation, email, phoneNumber)

    fun generateAppUser(
        id: UUID? = null,
        displayName: String = ("Test display name"),
        password: String = ("Test password"),
        email: String = ("test@email.com"),
        phoneNumber: String = ("(980)-111-1111")
    ) = CodeFriendzAppUser(displayName, password, email, phoneNumber, id)
}
