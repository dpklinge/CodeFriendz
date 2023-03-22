package com.codefriendz.errors.validation

import com.codefriendz.errors.InvalidInputError

sealed class ValidationError(message: String, val validationErrorType: ValidationErrorType) : InvalidInputError(message = message)

class NonMatchingPasswordsError(message: String = "Passwords did not match") : ValidationError(message, ValidationErrorType.NON_MATCHING_PASSWORDS)
class EmailAlreadyExistsError(message: String = "The provided email is already linked to an account.") : ValidationError(message, ValidationErrorType.EMAIL_ALREADY_EXISTS)
class PhoneNumberAlreadyExistsError(message: String = "The provided phone number is already linked to an account.") : ValidationError(message, ValidationErrorType.PHONE_NUMBER_ALREADY_EXISTS)

class ServerError(message: String = "There was an error processing your registration. If the error persists, contact support.") : ValidationError(message, ValidationErrorType.SERVER_ERROR)

enum class ValidationErrorType { NON_MATCHING_PASSWORDS, EMAIL_ALREADY_EXISTS, PHONE_NUMBER_ALREADY_EXISTS, SERVER_ERROR }
