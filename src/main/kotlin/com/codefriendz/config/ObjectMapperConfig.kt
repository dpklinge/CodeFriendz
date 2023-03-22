package com.codefriendz.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField


@Configuration
class ObjectMapperConfig {

    @Bean
    fun objectMapper() = jacksonObjectMapper().registerModule(JavaTimeModule().addSerializer(CustomSerializer()))

}


class CustomSerializer() : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
    private val formatter: DateTimeFormatter = DateTimeFormatterBuilder() // date/time
        .appendPattern("yyyy-MM-dd HH:mm:ss") // optional fraction of seconds (from 0 to 9 digits)
        .optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true).optionalEnd() // offset
        .appendPattern("x") // create formatter
        .toFormatter()

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeString(value.format(formatter))
    }


}