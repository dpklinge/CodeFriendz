package com.codefriendz.errors

import org.springframework.http.HttpStatus

open class AppServerError(val message: String, val errorCode: ErrorCode)

enum class ErrorCode(val statusCode: HttpStatus) {
    INVALID_INPUT(HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST),
    DATABASE_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    INFORMATION_NOT_FOUND(HttpStatus.NOT_FOUND)
}
