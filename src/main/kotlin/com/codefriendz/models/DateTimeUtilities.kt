package com.codefriendz.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

val STANDARD_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder() // date/time
    .appendPattern("yyyy-MM-dd HH:mm:ss") // optional fraction of seconds (from 0 to 9 digits)
    .optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true).optionalEnd() // offset
    .appendPattern("x") // create formatter
    .toFormatter()

suspend fun formatDateTime(time: LocalDateTime): String{
    return time.format(STANDARD_FORMATTER)
}
suspend fun LocalDateTime.toAppStandardString(): String{
    return formatDateTime(this)
}

suspend fun LocalDateTime.toAppStandardLocalDateTime(): LocalDateTime{
    return this.truncatedTo(ChronoUnit.MICROS)
}