package com.codefriendz.models

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.lang.IllegalArgumentException

private val phoneNumberUtil = PhoneNumberUtil.getInstance()
fun String.toStandardizedPhoneNumberFormat(): String {
    try {
        return phoneNumberUtil.format(phoneNumberUtil.parse(this, "US"), PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
    } catch (e: NumberParseException) {
        throw IllegalArgumentException("$this is an invalid phone number")
    }
}
