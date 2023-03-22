package com.codefriendz.controllers

import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Map<String, String>> {
        val value = ex.constraintViolations.map { Pair(it.propertyPath.last().name, it.message) }
        return ResponseEntity.badRequest().body(value.toMap())
    }

    @ExceptionHandler
    fun handleWebExchangeBindException(ex: WebExchangeBindException): ResponseEntity<Map<String, String>> {
        val value = ex.allErrors.map { Pair((if (it is FieldError) { it.field } else { it.objectName }), it.defaultMessage ?: "I don't know why you're seeing this") }
        return ResponseEntity.badRequest().body(value.toMap())
    }
}
