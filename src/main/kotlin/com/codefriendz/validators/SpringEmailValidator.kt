package com.codefriendz.validators

import org.apache.commons.validator.routines.EmailValidator
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

class SpringEmailValidator : ConstraintValidator<ValidEmail, String> {
    override fun isValid(
        email: String,
        context: ConstraintValidatorContext
    ): Boolean {
        return if (!EmailValidator.getInstance().isValid(email)) {
            // This disables the default message provided in @ValidEmail and supplies a new message.
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("Invalid email provided.").addConstraintViolation()
            false
        } else {
            true
        }
    }
}

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SpringEmailValidator::class])
@MustBeDocumented
annotation class ValidEmail(
    val message: String = "Invalid email provided.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
