package com.codefriendz.services

import com.codefriendz.errors.AppServerError
import com.codefriendz.errors.DatabaseAccessError
import com.codefriendz.errors.exceptions.InformationNotFoundException
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class DatabaseErrorHandler {
    private val logger = KotlinLogging.logger { }
    suspend fun handleException(exception: Throwable): AppServerError {
        return if (exception is InformationNotFoundException) {
            logger.debug { "Information not found: ${exception.error.message}" }
            exception.error
        } else {
            logger.warn(exception) { "Error accessing database: " }
            DatabaseAccessError()
        }
    }
}
