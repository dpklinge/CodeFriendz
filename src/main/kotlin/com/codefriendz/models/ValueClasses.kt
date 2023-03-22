package com.codefriendz.models

import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.apache.commons.validator.routines.EmailValidator

@JvmInline
value class DisplayName(val value: String) {
    init {
        require(value.length <= 50)
    }
}

@JvmInline
value class Password(val value: String)

@JvmInline
value class Email(val value: String) {
    init {
        require(EmailValidator.getInstance().isValid(value))
    }
}
private val phoneNumberUtil = PhoneNumberUtil.getInstance()

@JvmInline
value class PhoneNumber(val value: String) {
    init {
        require(phoneNumberUtil.isPossibleNumber(value, "US"))
    }
}

@JvmInline
value class Age(val value: Int) {
    init {
        require(value in 19..122)
    }
}

@JvmInline
value class Intention(val value: String) {
    init {
        require(value.length <= 50)
    }
}
