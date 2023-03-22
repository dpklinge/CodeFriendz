package com.codefriendz.controllers

import arrow.core.Either
import com.codefriendz.errors.AppServerError
import org.springframework.http.ResponseEntity

fun Either<AppServerError, Any>.toResponseEntity() = this.fold({ ResponseEntity.status(it.errorCode.statusCode).body(it) }, { ResponseEntity.ok().body(it) })
