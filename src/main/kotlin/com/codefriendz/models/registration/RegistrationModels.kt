package com.codefriendz.models.registration

import com.codefriendz.errors.validation.ValidationError
import com.codefriendz.models.user.CodeFriendzAppUser
import com.codefriendz.validators.ValidEmail
import com.codefriendz.validators.ValidPhoneNumber
import javax.validation.constraints.*

data class RegistrationInput(
    @field:Size(min = 2, max = 15, message = "Display name has invalid length - length should be between 2-15 characters")
    val displayName: String,
    @field:Size(min = 12, message = "Password too short, must be at least 12 characters")
    val password: String,
    val passwordConfirmation: String,
    @ValidEmail
    val email: String,
    @ValidPhoneNumber
    val phoneNumber: String
)
data class RegistrationOutcome(
    val didSucceed: Boolean,
    val errors: List<ValidationError>,
    val registeredUser: CodeFriendzAppUser?
)
