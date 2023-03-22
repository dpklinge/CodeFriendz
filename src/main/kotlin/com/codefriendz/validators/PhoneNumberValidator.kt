package com.codefriendz.validators
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

class PhoneNumberValidator : ConstraintValidator<ValidPhoneNumber, String> {
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()
    override fun isValid(
        phoneNumber: String,
        context: ConstraintValidatorContext
    ): Boolean {
        return if (!phoneNumberUtil.isPossibleNumber(phoneNumber, "US")) {
            // This disables the default message provided in @ValidPhoneNumber and supplies a new message.
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("Invalid phone number provided.").addConstraintViolation()
            false
        } else {
            true
        }
    }
}

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PhoneNumberValidator::class])
@MustBeDocumented
annotation class ValidPhoneNumber(
    val message: String = "Invalid phone number provided.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
